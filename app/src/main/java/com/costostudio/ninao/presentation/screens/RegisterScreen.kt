package com.costostudio.ninao.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.costostudio.ninao.R
import com.costostudio.ninao.presentation.compose.AppLogo
import com.costostudio.ninao.presentation.compose.BackgroundApplicationImage
import com.costostudio.ninao.presentation.compose.CustomButton
import com.costostudio.ninao.presentation.compose.CustomLoading
import com.costostudio.ninao.presentation.compose.CustomTextButton
import com.costostudio.ninao.presentation.compose.CustomTextField
import com.costostudio.ninao.presentation.events.AuthenticationUiEvent
import com.costostudio.ninao.presentation.events.RegisterEvent
import com.costostudio.ninao.presentation.navigation.AuthNavigator
import com.costostudio.ninao.presentation.uistate.RegisterUiState
import com.costostudio.ninao.presentation.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigator: AuthNavigator,
) {
    val registerUiState by viewModel.registerUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.registerUiEvent.collectLatest { event ->
            when (event) {
                is AuthenticationUiEvent.Success -> navigator.navigateToLogin()
                is AuthenticationUiEvent.ShowError -> Toast.makeText(
                    context, event.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    RegisterScreenContent(
        registerUiState = registerUiState,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = navigator::navigateToLogin
    )
}

@Composable
fun RegisterScreenContent(
    registerUiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BackgroundApplicationImage(
            backgroundImage = R.drawable.bg,
            modifier = Modifier.matchParentSize()
        )

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

            Text(
                text = stringResource(R.string.registerScreen_createAccount),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = registerUiState.firstName,
                onValueChange = { onEvent(RegisterEvent.FirstNameChanged(it)) },
                label = stringResource(R.string.registerScreen_firstname),
                leadingIcon = Icons.Default.Abc,
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = registerUiState.lastName,
                onValueChange = { onEvent(RegisterEvent.LastNameChanged(it)) },
                label = stringResource(R.string.registerScreen_lastname),
                leadingIcon = Icons.Default.Abc,
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = registerUiState.email,
                onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                label = stringResource(R.string.registerScreen_email),
                leadingIcon = Icons.Default.Email,
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = registerUiState.password,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                label = stringResource(R.string.registerScreen_password),
                leadingIcon = Icons.Default.Lock,
                trailingIcon = Icons.Default.Visibility,
                isPasswordField = true,
                isPasswordVisible = registerUiState.isPasswordVisible,
                onTogglePasswordVisibility = { onEvent(RegisterEvent.TogglePasswordVisibility) },
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = registerUiState.confirmPassword,
                onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                label = stringResource(R.string.registerScreen_confirmPassword),
                leadingIcon = Icons.Default.Lock,
                trailingIcon = Icons.Default.Visibility,
                isPasswordField = true,
                isPasswordVisible = registerUiState.isConfirmPasswordVisible,
                onTogglePasswordVisibility = { onEvent(RegisterEvent.ToggleConfirmPasswordVisibility) },
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = stringResource(R.string.registerScreen_signup),
                onClick = { onEvent(RegisterEvent.Register) },
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextButton(
                text = stringResource(R.string.registerScreen_haveAccount),
                onClick = onNavigateToLogin,
                style = MaterialTheme.typography.bodyMedium
            )

            if (registerUiState.baseScreenUiState.isLoading) {
                CustomLoading()
            }

        }
    }

}


@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreenContent(
        registerUiState = RegisterUiState(),
        onEvent = {},
        onNavigateToLogin = { })
}

