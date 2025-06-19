package com.costostudio.ninao.data.repository.image

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.costostudio.ninao.domain.repository.image.ImageRepository
import com.costostudio.ninao.domain.util.CustomResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageRepositoryImpl(
    private val context: Context
) : ImageRepository {

    override suspend fun saveImageFromUri(uri: Uri): Flow<CustomResource<String>> = flow {
        emit(CustomResource.Loading())

        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "profile_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
            val file = File(context.getExternalFilesDir(null), fileName)

            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            emit(CustomResource.Success(file.absolutePath))
        } catch (e: IOException) {
            emit(CustomResource.Error("Failed to save image: ${e.message}"))
        } catch (e: Exception) {
            emit(CustomResource.Error("Unexpected error occurred: ${e.message}"))
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