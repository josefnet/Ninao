package com.costostudio.ninao.di

import com.costostudio.ninao.presentation.viewmodel.LoginViewModel
import com.costostudio.ninao.presentation.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}