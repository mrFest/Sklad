package com.yourorg.aidwarehouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.yourorg.aidwarehouse.ui.MainScreen
import com.yourorg.aidwarehouse.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = lightColors(
                    primary      = Color.Black,
                    onPrimary    = Color.White,
                    secondary    = Color.Black,
                    onSecondary  = Color.White,
                    background   = Color.White,
                    onBackground = Color.Black
                )
            ) {
                Surface {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
