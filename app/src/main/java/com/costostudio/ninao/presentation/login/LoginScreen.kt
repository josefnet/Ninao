package com.costostudio.ninao.presentation.login

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costostudio.ninao.R
import com.costostudio.ninao.presentation.util.compose.AppLogo
import com.costostudio.ninao.presentation.util.compose.BackgroundApplicationImage
import com.costostudio.ninao.presentation.util.compose.CustomButton
import com.costostudio.ninao.presentation.util.compose.CustomLoading
import com.costostudio.ninao.presentation.util.compose.CustomTextButton
import com.costostudio.ninao.presentation.util.compose.CustomTextField
import com.costostudio.ninao.presentation.util.events.AuthenticationUiEvent
import com.costostudio.ninao.presentation.navigation.AuthNavigator
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigator: AuthNavigator
) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loginUiEvent.collectLatest { event ->
            when (event) {
                is AuthenticationUiEvent.Success -> navigator.navigateToHome()
                is AuthenticationUiEvent.ShowError -> Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LoginScreenContent(
        loginUiState = loginUiState,
        onEvent= viewModel::onEvent,
        onNavigateToRegister = navigator::navigateToRegister
    )
}

@Composable
fun LoginScreenContent(
    loginUiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit
) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BackgroundApplicationImage(
                backgroundImage = R.drawable.bg,
                modifier = Modifier.matchParentSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AppLogo(
                    backgroundImage = R.drawable.logo,
                    modifier = Modifier.fillMaxWidth()
                )


                Text(text = stringResource(R.string.loginScreen_login_message), fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = loginUiState.email,
                    onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                    label = stringResource(R.string.registerScreen_email),
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier.fillMaxWidth()
                )


                CustomTextField(
                    value = loginUiState.password,
                    onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                    label = stringResource(R.string.registerScreen_password),
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = Icons.Default.Visibility,
                    isPasswordField = true,
                    isPasswordVisible = loginUiState.isPasswordVisible,
                    onTogglePasswordVisibility = { onEvent(LoginEvent.TogglePasswordVisibility) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))


                CustomButton(
                    text = stringResource(R.string.loginScreen_signIn),
                    onClick = { onEvent(LoginEvent.Submit) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ContinueWithButton(
                    buttonText = stringResource(R.string.loginScreen_signInWithGoogle),
                    id = R.drawable.google_login
                ) {
                    /* viewModel.signInWithGoogle(activity) { signInIntent ->
                          googleSignInLauncher.launch(signInIntent) // Use the intent directly here
                      }*/
                }

                CustomTextButton(
                    text = stringResource(R.string.loginScreen_newAccount),
                    onClick = onNavigateToRegister,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (loginUiState.baseScreenUiState.isLoading) {
                    CustomLoading()
                }
        }
    }

}

@Composable
fun ContinueWithButton(
    buttonText: String,
    @DrawableRes id: Int,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id),
                    contentDescription = buttonText,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = buttonText,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        loginUiState = LoginUiState(),
        onEvent = {},
        onNavigateToRegister = {}
    )
}
