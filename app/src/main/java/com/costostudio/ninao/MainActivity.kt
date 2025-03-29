package com.costostudio.ninao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.costostudio.ninao.presentation.navigation.AuthNavGraph
import com.costostudio.ninao.presentation.ui.theme.NinaoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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