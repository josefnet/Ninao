package com.costostudio.ninao.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.costostudio.ninao.R
import com.costostudio.ninao.presentation.util.compose.CustomTextField
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val viewModel: ProfileViewModel = koinViewModel()
    val profileUiState by viewModel.profileUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.profileUiState.collectLatest { state ->
            when {
                state.isSuccess -> {
                    Toast.makeText(context, "Modification réussie", Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(ProfileUiEvent.ClearSuccess)
                }
                !state.errorMessage.isNullOrEmpty() -> {
                    Toast.makeText(context, "Erreur : ${state.errorMessage}", Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(ProfileUiEvent.ClearError)
                }
            }
        }
    }

    ProfileScreenContent(
        profileUiState = profileUiState,
        onEvent = viewModel::onEvent
    )

}

@Composable
fun ProfileScreenContent(
    profileUiState: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "👤 Mon Profil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profileUiState.firstName + " " + profileUiState.lastName.capitalize(),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Informations du profil
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                CustomTextField(
                    value = profileUiState.firstName,
                    onValueChange = { onEvent(ProfileUiEvent.FirstNameChanged(it)) },
                    label = stringResource(R.string.registerScreen_firstname),
                    leadingIcon = Icons.Default.Abc,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = profileUiState.lastName,
                    onValueChange = { onEvent(ProfileUiEvent.LastNameChanged(it)) },
                    label = stringResource(R.string.registerScreen_lastname),
                    leadingIcon = Icons.Default.Abc,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEvent(ProfileUiEvent.SaveModification) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sauvegarder les modifications")
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        profileUiState = ProfileUiState(),
        onEvent = {}
    )
}