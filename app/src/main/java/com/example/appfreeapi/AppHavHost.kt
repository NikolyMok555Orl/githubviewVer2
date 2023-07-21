package com.example.appfreeapi

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appfreeapi.login.LoginScreenUI
import com.example.appfreeapi.repositories.SearchRepoScreenUI
import com.example.appfreeapi.user.UserScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavHost.START
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        composable(NavHost.START) {
            LoginScreenUI(navController)
        }
        //Все маршруты
        composable(NavHost.REPOIES) { SearchRepoScreenUI(navController) }
        //Маршрут
        composable(
            "${NavHost.USER}/{${NavHost.USER_URL}}",
            arguments = listOf(navArgument(NavHost.USER_URL) { type = NavType.StringType })
        ) {
            UserScreen(navController, it.arguments?.getString(NavHost.USER_URL) ?: "")
        }


    }



}








object NavHost {
    const val START = "login"
    const val REPOIES = "repositories"
    const val USER = "user"
    const val USER_URL = "user_url"

}