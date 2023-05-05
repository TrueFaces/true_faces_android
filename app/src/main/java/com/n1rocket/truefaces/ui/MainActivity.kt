package com.n1rocket.truefaces.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.n1rocket.truefaces.ui.screens.avatar.AvatarScreen
import com.n1rocket.truefaces.ui.screens.login.LoginScreen
import com.n1rocket.truefaces.ui.screens.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authorizationViewModel: AuthorizationViewModel = hiltViewModel()

            val tokenState = authorizationViewModel.state.collectAsState()

            if (tokenState.value.isEmpty()) {
                Log.d("MainActivity", "TokenState: ${tokenState.value}")
            }

            NavHost(
                navController = navController,
                startDestination = getStartDestination(authorizationViewModel),
            ) {
                composable(Routes.Login.route) {
                    LoginScreen(
                        onLoggedSuccess = { hasAvatar ->

                            if (hasAvatar) {
                                navController.navigate(Routes.Main.route) {
                                    popUpTo(Routes.Login.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                navController.navigate(Routes.Avatar.route) {
                                    popUpTo(Routes.Login.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }, hiltViewModel()
                    )
                }
                composable(Routes.Avatar.route) { AvatarScreen(hiltViewModel()){
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Login.route) {
                            inclusive = true
                        }
                    }
                } }
                composable(Routes.Main.route) { MainScreen(hiltViewModel()) }
            }
        }
    }

    private fun getStartDestination(authorizationViewModel: AuthorizationViewModel): String = when {
        authorizationViewModel.isLogged() ->
            if (authorizationViewModel.hasAvatar()) {
                Routes.Main.route
            } else {
                Routes.Avatar.route
            }

        else -> Routes.Login.route
    }

}

