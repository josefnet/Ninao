package com.costostudio.ninao.data.repository.image

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.costostudio.ninao.domain.repository.image.ImageRepository
import com.costostudio.ninao.domain.util.CustomResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File

class ImageRepositoryImpl(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ImageRepository {

    override suspend fun saveImageFromUri(uri: Uri): Flow<CustomResource<String>> = flow {
        emit(CustomResource.Loading())

        try {
            // Vérifier que l'utilisateur est connecté
            val user = firebaseAuth.currentUser
            if (user == null) {
                emit(CustomResource.Error("Utilisateur non authentifié."))
                return@flow
            }

            // Créer un nom de fichier unique lié à l'utilisateur
            val fileName = "profile_${user.uid}_${System.currentTimeMillis()}.jpg"
            val imageRef = storage.reference.child("images/$fileName")

            // Upload du fichier
            imageRef.putFile(uri).await()

            // Récupération de l'URL de téléchargement
            val downloadUrl = imageRef.downloadUrl.await().toString()

            emit(CustomResource.Success(downloadUrl))
        } catch (e: Exception) {
            emit(CustomResource.Error("Échec de l’upload de l’image: ${e.localizedMessage}"))
        }
    }

    override suspend fun createTempImageUri(): Uri {
        val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.getExternalFilesDir(null), fileName)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}