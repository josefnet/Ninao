package com.costostudio.ninao.domain.usecase.image

import android.net.Uri
import com.costostudio.ninao.domain.repository.image.ImageRepository
import com.costostudio.ninao.domain.util.CustomResource
import kotlinx.coroutines.flow.Flow

class CaptureImageFromCameraUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(uri: Uri): Flow<CustomResource<String>> {
        return imageRepository.saveImageFromUri(uri)
    }
}
