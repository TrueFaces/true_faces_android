package com.n1rocket.truefaces.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.n1rocket.truefaces.ui.screens.login.LoginScreen
import com.n1rocket.truefaces.ui.screens.main.MainScreen
import com.n1rocket.truefaces.ui.theme.TrueFacesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authorizationViewModel: AuthorizationViewModel = hiltViewModel()
            NavHost(
                navController = navController,
                startDestination = if (authorizationViewModel.isLogged()) Routes.Main.route else Routes.Login.route,
            ) {
                composable(Routes.Login.route) { LoginScreen(navController, hiltViewModel()) }
                composable(Routes.Main.route) { MainScreen(navController, hiltViewModel()) }
            }
        }
    }
}

