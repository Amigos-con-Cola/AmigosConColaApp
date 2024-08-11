package io.github.amigosconcola.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.amigosconcola.R

@Composable
fun AnimalAvatar(
    url: String?,
    contentDescription: String,
    species: String,
    modifier: Modifier = Modifier,
) {
    val defaultAvatar = if (species == "Cat") R.drawable.default_cat else R.drawable.default_dog

    AsyncImage(
        modifier = modifier.height(200.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        fallback = painterResource(defaultAvatar),
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
    )
}