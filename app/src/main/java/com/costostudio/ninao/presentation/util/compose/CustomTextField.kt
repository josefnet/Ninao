package com.costostudio.ninao.presentation.util.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            visualTransformation = when {
                isPasswordField && !isPasswordVisible -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(imageVector = it, contentDescription = "Leading Icon")
                }
            },
            trailingIcon = {
                when {
                    isPasswordField && onTogglePasswordVisibility != null -> {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible) "Masquer le mot de passe" else "Afficher le mot de passe"
                            )
                        }
                    }

                    trailingIcon != null && onTrailingIconClick != null -> {
                        IconButton(onClick = onTrailingIconClick) {
                            Icon(imageVector = trailingIcon, contentDescription = "Trailing Icon")
                        }
                    }

                    else -> {}
                }
            },
            isError = errorMessage != null,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview(){
    CustomTextField(
        value = "Youssef",
        onValueChange = {},
        label = "FirstName",
        isPasswordField = false,
        isPasswordVisible = false,
    )
}