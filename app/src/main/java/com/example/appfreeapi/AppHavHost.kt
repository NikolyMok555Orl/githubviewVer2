package com.example.appfreeapi

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appfreeapi.NavHost.PROFILE
import com.example.appfreeapi.NavHost.REPOIES
import com.example.appfreeapi.NavHost.itemsMenu
import com.example.appfreeapi.login.LoginScreenUI
import com.example.appfreeapi.profile.ui.ProfileScreenUI
import com.example.appfreeapi.repositories.SearchRepoScreenUI
import com.example.appfreeapi.user.UserScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavHost.START
) {
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = bottomBarState.value) {
                BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    itemsMenu.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null)},
                            label = { },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = startDestination,
            Modifier.padding(innerPadding)
        ) {
            composable(ScreenMenuUI.Main.route) {
                bottomBarState.value = true
                SearchRepoScreenUI(navController)
            }
            composable(ScreenMenuUI.Profile.route) {
                bottomBarState.value = true
                ProfileScreenUI(navController)
            }
            composable(NavHost.START) {
                bottomBarState.value = false
                LoginScreenUI(navController)
            }
            composable(
                "${NavHost.USER}/{${NavHost.USER_URL}}",
                arguments = listOf(navArgument(NavHost.USER_URL) { type = NavType.StringType })
            ) {
                bottomBarState.value = true
                UserScreen(navController, it.arguments?.getString(NavHost.USER_URL) ?: "")
            }
        }
    }


}

sealed class ScreenMenuUI(val route: String, val icon: ImageVector) {
    object Main : ScreenMenuUI(REPOIES, Icons.Default.Home)
    object Profile : ScreenMenuUI(PROFILE, Icons.Default.Person)
}


object NavHost {
    const val START = "login"
    const val REPOIES = "repositories"
    const val USER = "user"
    const val PROFILE = "profiel"
    const val USER_URL = "user_url"
    val itemsMenu = listOf(
        ScreenMenuUI.Main,
        ScreenMenuUI.Profile,
    )
}