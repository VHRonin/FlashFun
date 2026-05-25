package com.example.flashfun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.flashfun.data.Repository
import com.example.flashfun.navigation.FlashCardNavHost
import com.example.flashfun.ui.theme.FlashFunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Repository.load(context = this)
        setContent {
            FlashFunTheme {
                val navController = rememberNavController()
                FlashCardNavHost(navController = navController)
            }
        }
    }
}