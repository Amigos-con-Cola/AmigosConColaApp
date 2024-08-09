package io.github.amigosconcola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.amigosconcola.ui.screens.HomeScreen
import io.github.amigosconcola.ui.screens.LoginScreen
import io.github.amigosconcola.ui.screens.Screen
import io.github.amigosconcola.ui.theme.AmigosConColaTheme
import io.github.amigosconcola.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.flow.combine

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tokenViewModel: TokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

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

            AmigosConColaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home
                    ) {
                        composable(Screen.Login) {
                            LoginScreen(
                                navController,
                                tokensViewModel = tokenViewModel,
                            )
                        }

                        composable(Screen.Home) {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}