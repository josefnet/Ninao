package com.costostudio.ninao.presentation.util.compose

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(fontSize = 14.sp)
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text, style = style)
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextButtonPreview() {
    CustomTextButton(
        text = "Déjà un compte ? Se connecter",
        onClick = {}
    )
}