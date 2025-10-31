package com.example.salo_project_warning


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


object AppRoutes{
    const val  HOME = "home"
    const val  REMEMBER = "remember"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.HOME


                    ) {
                        composable(AppRoutes.HOME) {

                            HomePage(

                                onNavigateToRememberPage = {

                                    navController.navigate(AppRoutes.REMEMBER)
                                }
                            )
                        }
                        composable(AppRoutes.REMEMBER) {

                            RememberPage(

                                onNavigateBack = {
                                    navController.popBackStack()
                                }


                            )


                        }
                    }
                }
            }
        }


    }
}