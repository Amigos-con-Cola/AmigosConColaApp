package io.github.amigosconcola.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.amigosconcola.R
import io.github.amigosconcola.ui.screens.Screen
import io.github.amigosconcola.ui.viewmodel.TokenUiEvent
import io.github.amigosconcola.ui.viewmodel.TokenViewModel
import kotlinx.coroutines.launch


data class NavigationItem(
    val icon: ImageVector,
    val name: String,
    val screen: String,
)

@Composable
fun Drawer(
    navController: NavController,
    drawerState: DrawerState,
    tokenViewModel: TokenViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    val drawerLinks = listOf(
        NavigationItem(Icons.Default.Home, "Inicio", Screen.Home),
        NavigationItem(Icons.Default.Info, "Acerca", Screen.About)
    )

    ModalDrawerSheet(
        drawerShape = RectangleShape,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        for ((icon, name, screen) in drawerLinks) {
            DrawerLink(
                icon = icon,
                name = name,
                onClick = { navController.navigate(screen) },
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.weight(1.0f))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                tokenViewModel.onEvent(TokenUiEvent.Delete)
                scope.launch {
                    drawerState.close()
                }
            }
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DrawerLink(
    icon: ImageVector,
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = name)
    }
}