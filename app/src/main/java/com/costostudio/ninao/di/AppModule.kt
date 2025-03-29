package com.costostudio.ninao.di

import com.costostudio.ninao.presentation.viewmodel.HomeViewModel
import com.costostudio.ninao.presentation.viewmodel.LoginViewModel
import com.costostudio.ninao.presentation.viewmodel.RegisterViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single<SignInClient> { Identity.getSignInClient(androidContext()) }
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { HomeViewModel() }
}