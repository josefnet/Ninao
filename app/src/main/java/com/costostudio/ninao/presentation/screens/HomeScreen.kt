package com.costostudio.ninao.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costostudio.ninao.R
import com.costostudio.ninao.presentation.navigation.AuthNavigator
import com.costostudio.ninao.presentation.uistate.HomeUiState
import com.costostudio.ninao.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigator: AuthNavigator,

) {
    val state by viewModel.uiState.collectAsState()
    HomeScreenContent(
        state = state,
        onLogout = viewModel::logout,
        onNavigateToLogin = navigator::navigateToLogin
    )
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    if (!state.isLoggedIn) {
        LaunchedEffect(Unit) {
            onNavigateToLogin()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenue\n ${state.userEmail}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onLogout) {
                    Text("Se déconnecter")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeState = HomeUiState(
        isLoggedIn = true,
        userEmail = "preview@user.com"
    )

    HomeScreenContent(
        state = fakeState,
        onLogout = {},
        onNavigateToLogin = {}
    )
}