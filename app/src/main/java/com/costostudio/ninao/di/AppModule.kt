package com.costostudio.ninao.di

import com.costostudio.ninao.data.repository.AuthRepositoryImpl
import com.costostudio.ninao.data.repository.UserRepositoryImpl
import com.costostudio.ninao.domain.repository.AuthRepository
import com.costostudio.ninao.domain.repository.UserRepository
import com.costostudio.ninao.domain.usecase.LoginUseCase
import com.costostudio.ninao.domain.usecase.LoginUseCaseImpl
import com.costostudio.ninao.domain.usecase.RegisterUseCase
import com.costostudio.ninao.domain.usecase.RegisterUseCaseImpl
import com.costostudio.ninao.domain.usecase.SaveUserToFireStoreUseCase
import com.costostudio.ninao.domain.usecase.SaveUserToFireStoreUseCaseImpl
import com.costostudio.ninao.presentation.login.LoginViewModel
import com.costostudio.ninao.presentation.register.RegisterViewModel
import com.costostudio.ninao.presentation.splash.SplashViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    single<SignInClient> { Identity.getSignInClient(androidContext()) }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    // useCases
    single<LoginUseCase> { LoginUseCaseImpl(get()) }
    single<RegisterUseCase> { RegisterUseCaseImpl(get()) }
    single<SaveUserToFireStoreUseCase> { SaveUserToFireStoreUseCaseImpl(get()) }

    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get(),get())}
    viewModel { SplashViewModel() }
}