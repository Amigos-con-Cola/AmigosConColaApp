package io.github.amigosconcola.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.amigosconcola.R
import io.github.amigosconcola.ui.composables.AppBar
import kotlinx.coroutines.launch

@Composable
fun AboutScreen(
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val onMenuClicked: () -> Unit = {
        scope.launch {
            drawerState.open()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = { AppBar(onMenuClicked = onMenuClicked) },
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier.padding(innerPaddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )

            AboutItem("Versión 0.0.1")
            AboutItem("Guayaquil, Ecuador")

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Todos los derechos reservados 2024 Amigos con Cola",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun AboutItem(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.padding(
            vertical = 16.dp,
            horizontal = 8.dp
        )
    )
}