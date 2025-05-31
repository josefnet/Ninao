package com.costostudio.ninao

import android.app.Application
import android.util.Log
import com.costostudio.ninao.di.appModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialisation de Koin
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
        FirebaseApp.initializeApp(this)
        val firebaseApp = FirebaseApp.getInstance()
        Log.d("FirebaseAuth", "FirebaseApp initialized: ${firebaseApp.name}")
    }
}