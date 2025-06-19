package com.costostudio.ninao.domain.repository.image

import android.net.Uri
import com.costostudio.ninao.domain.util.CustomResource
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun saveImageFromUri(uri: Uri): Flow<CustomResource<String>>
    suspend fun createTempImageUri(): Uri
}