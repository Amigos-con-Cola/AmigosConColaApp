package io.github.amigosconcola.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.amigosconcola.R

@Composable
fun AnimalGender(
    gender: String
) {
    val genderIcon = if (gender == "Male") R.drawable.male else R.drawable.female
    val genderIconBackgroundColor = if (gender == "Male") Color(0xffdae3f3) else Color(0xfff0dbe4)

    Box(
        modifier = Modifier
            .height(36.dp)
            .aspectRatio(1f)
            .background(
                color = genderIconBackgroundColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(genderIcon),
            contentDescription = "gender",
            contentScale = ContentScale.Inside,
            modifier = Modifier.height(24.dp)
        )
    }
}