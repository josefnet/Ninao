package com.costostudio.ninao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.costostudio.ninao.presentation.navigation.AuthNavGraph
import com.costostudio.ninao.presentation.splash.SplashViewModel
import com.costostudio.ninao.presentation.ui.theme.NinaoTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val splashViewModel: SplashViewModel by viewModel()
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }
        enableEdgeToEdge()
        setContent {
            NinaoTheme {
                AuthNavGraph()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    NinaoTheme {}
}