package com.costostudio.ninao.di

import com.costostudio.ninao.data.repository.AuthRepositoryImpl
import com.costostudio.ninao.data.repository.UserRepositoryImpl
import com.costostudio.ninao.data.repository.image.ImageRepositoryImpl
import com.costostudio.ninao.domain.repository.AuthRepository
import com.costostudio.ninao.domain.repository.UserRepository
import com.costostudio.ninao.domain.repository.image.ImageRepository
import com.costostudio.ninao.domain.usecase.GetUserUseCase
import com.costostudio.ninao.domain.usecase.GetUserUseCaseImpl
import com.costostudio.ninao.domain.usecase.LoginUseCase
import com.costostudio.ninao.domain.usecase.LoginUseCaseImpl
import com.costostudio.ninao.domain.usecase.RegisterUseCase
import com.costostudio.ninao.domain.usecase.RegisterUseCaseImpl
import com.costostudio.ninao.domain.usecase.SaveUserToFireStoreUseCase
import com.costostudio.ninao.domain.usecase.SaveUserToFireStoreUseCaseImpl
import com.costostudio.ninao.domain.usecase.UpdateUserToFireStoreUseCase
import com.costostudio.ninao.domain.usecase.UpdateUserToFireStoreUseCaseImpl
import com.costostudio.ninao.domain.usecase.image.CaptureImageFromCameraUseCase
import com.costostudio.ninao.domain.usecase.image.SelectImageFromGalleryUseCase
import com.costostudio.ninao.presentation.login.LoginViewModel
import com.costostudio.ninao.presentation.profile.ProfileViewModel
import com.costostudio.ninao.presentation.register.RegisterViewModel
import com.costostudio.ninao.presentation.splash.SplashViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }

    single<SignInClient> { Identity.getSignInClient(androidContext()) }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(),get()) }
    single<ImageRepository> { ImageRepositoryImpl(androidContext(),get(),get()) }

    // useCases
    single<LoginUseCase> { LoginUseCaseImpl(get()) }
    single<RegisterUseCase> { RegisterUseCaseImpl(get()) }
    single<SaveUserToFireStoreUseCase> { SaveUserToFireStoreUseCaseImpl(get()) }
    single<UpdateUserToFireStoreUseCase> { UpdateUserToFireStoreUseCaseImpl(get()) }
    single<GetUserUseCase> { GetUserUseCaseImpl(get()) }
    factory { SelectImageFromGalleryUseCase(get()) }
    factory { CaptureImageFromCameraUseCase(get()) }

    // ViewModels
    viewModel { SplashViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get(),get())}
    viewModel { ProfileViewModel(get(),get(),get(),get()) }
}