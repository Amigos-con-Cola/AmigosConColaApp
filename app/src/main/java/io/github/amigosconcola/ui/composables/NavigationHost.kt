package io.github.amigosconcola.ui.composables

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.amigosconcola.ui.screens.AboutScreen
import io.github.amigosconcola.ui.screens.HomeScreen
import io.github.amigosconcola.ui.screens.LoginScreen
import io.github.amigosconcola.ui.screens.Screen
import io.github.amigosconcola.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.flow.combine

@Composable
fun NavigationHost(
    navController: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    tokenViewModel: TokenViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) {
        tokenViewModel
            .tokens
            .combine(navController.currentBackStackEntryFlow) { tokens, backStackEntry ->
                tokens to backStackEntry
            }
            .collect { (tokens, backStackEntry) ->
                val route = backStackEntry.destination.route

                if (tokens != null && route == Screen.Login) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) {
                            inclusive = true
                        }
                    }
                }

                if (tokens == null && route != Screen.Login) {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Home) {
                            inclusive = true
                        }
                    }
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable(Screen.Login) {
            LoginScreen(
                navController,
                tokensViewModel = tokenViewModel,
            )
        }

        composable(Screen.Home) {
            HomeScreen(
                drawerState
            )
        }

        composable(Screen.About) {
            AboutScreen()
        }
    }
}