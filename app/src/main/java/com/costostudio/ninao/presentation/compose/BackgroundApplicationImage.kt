package com.costostudio.ninao.presentation.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun BackgroundApplicationImage(
    @DrawableRes backgroundImage: Int,
    modifier: Modifier = Modifier,

) {
    Image(
        painter = painterResource(backgroundImage),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

