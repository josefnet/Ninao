package com.costostudio.ninao.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.costostudio.ninao.R
import com.costostudio.ninao.presentation.util.compose.ClickableProfileImageWithLabel
import com.costostudio.ninao.presentation.util.compose.CustomTextField
import com.costostudio.ninao.presentation.util.compose.GenreSelectionComponent
import com.costostudio.ninao.presentation.util.compose.ImageSourceDialog
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val viewModel: ProfileViewModel = koinViewModel()
    val profileUiState by viewModel.profileUiState.collectAsState()
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }

    // Camera Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri.value?.let { uri ->
                viewModel.onEvent(ProfileUiEvent.Image.OnImageCaptured(uri))
            }
        }
    }

    // launcher pour demander la permission CAMERA
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Si permission accordée, on lance la caméra avec l'URI temporaire
                imageUri.value?.let { uri ->
                    cameraLauncher.launch(uri)
                }
            } else {
                Toast.makeText(context, "Permission caméra refusée", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Fonction pour créer l'URI temporaire (copie de ta fonction)
    fun createCameraUri(): Uri {
        val fileName = "camera_image_${
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        }.jpg"
        val file = File(context.getExternalFilesDir(null), fileName)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    // Fonction pour lancer la caméra en vérifiant la permission
    fun checkPermissionAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Permission déjà accordée
            val uri = createCameraUri()
            imageUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            // Demande la permission et prépare l'URI en avance
            val uri = createCameraUri()
            imageUri.value = uri
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }


    // Gallery Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onEvent(ProfileUiEvent.Image.OnImageSelected(it)) }
    }

    LaunchedEffect(profileUiState.imageUiState.error) {
        // to get error here
        profileUiState.imageUiState.error?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            //Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.profileUiState.collectLatest { state ->
            when {
                state.isSuccess -> {
                    Toast.makeText(context, "Modification réussie", Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(ProfileUiEvent.ClearSuccess)
                }

                !state.errorMessage.isNullOrEmpty() -> {
                    Toast.makeText(context, "Erreur : ${state.errorMessage}", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.onEvent(ProfileUiEvent.ClearError)
                }
            }
        }
    }

    ProfileScreenContent(
        profileUiState = profileUiState,
        snackBarHostState = snackBarHostState,
        galleryLauncher = galleryLauncher,
        onCameraClick = {
            checkPermissionAndLaunchCamera()
        },
        onEvent = viewModel::onEvent
    )

}

@Composable
fun ProfileScreenContent(
    profileUiState: ProfileUiState,
    snackBarHostState: SnackbarHostState,
    galleryLauncher: ActivityResultLauncher<String>,
    onCameraClick: () -> Unit,
    onEvent: (ProfileUiEvent) -> Unit,
) {
    var selectedGender by remember { mutableStateOf("Homme") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
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

                ClickableProfileImageWithLabel(
                    imageUrl = profileUiState.imageUiState.imageUrl,
                    onClick = {
                        onEvent(ProfileUiEvent.Image.OnImageClicked)
                    }
                )
                if (profileUiState.imageUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            // Image source dialog
            if (profileUiState.imageUiState.showImageSourceDialog) {
                ImageSourceDialog(
                    onDismiss = { onEvent(ProfileUiEvent.Image.OnImageSourceDialogDismissed) },
                    onGalleryClick = {
                        onEvent(ProfileUiEvent.Image.OnGallerySelected)
                        galleryLauncher.launch("image/*")
                    },
                    onCameraClick = {
                        onEvent(ProfileUiEvent.Image.OnCameraSelected)
                        onCameraClick()
                    }
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

                    GenreSelectionComponent(
                        selectedGender = selectedGender,
                        onGenderSelected = { selectedGender = it }
                    )

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

                    CustomTextField(
                        value = profileUiState.email,
                        onValueChange = { onEvent(ProfileUiEvent.EmailChanged(it)) },
                        label = stringResource(R.string.registerScreen_email),
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
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
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val context = LocalContext.current

    // Simuler un launcher de caméra avec callback vide
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        // Ne rien faire pour la preview
    }

    // Simuler un launcher de galerie avec callback vide
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // Ne rien faire pour la preview
    }

    ProfileScreenContent(
        profileUiState = ProfileUiState(),
        snackBarHostState = remember { SnackbarHostState() },
        galleryLauncher = galleryLauncher,
        onCameraClick = {},
        onEvent = {}
    )
}