package com.itis.finappproject.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.feature.home.impl.ui.game.GameScreen
import com.feature.home.impl.ui.game.LobbyScreen
import com.feature.home.impl.ui.home.HomeScreen
import com.feature.home.impl.ui.rules.RulesScreen
import com.feature.materials.impl.ui.DetailMaterialScreen
import com.feature.materials.impl.ui.MaterialScreen
import com.feature.profile.impl.ui.auth.signin.SignInScreen
import com.feature.profile.impl.ui.auth.signup.SignUpScreen
import com.feature.profile.impl.ui.profile.ProfileScreen

sealed class Screen(
    val route: String,
    val name: String,
    val icon: ImageVector,
) {
    object Home : Screen(
        route = "home",
        name = "Главная",
        icon = Icons.Filled.Home,
    )
    object Lobby : Screen(
        route = "lobby",
        name = "Лобби",
        icon = Icons.Filled.Home,
    )
    object Rules : Screen(
        route = "rules",
        name = "Правила",
        icon = Icons.Filled.Home,
    )
    object Game : Screen(
        route = "game",
        name = "Игра",
        icon = Icons.Filled.Home,
    )

    object Materials : Screen(
        route = "materials",
        name = "Материалы",
        icon = Icons.Filled.Menu,
    )
    object Material : Screen(
        route = "material",
        name = "Материал",
        icon = Icons.Filled.Menu,
    )

    object Profile : Screen(
        route = "profile",
        name = "Профиль",
        icon = Icons.Filled.AccountCircle,
    )
    object SignIn : Screen(
        route = "signin",
        name = "Вход",
        icon = Icons.Filled.Person,
    )
    object SignUp : Screen(
        route = "signup",
        name = "Регистрация",
        icon = Icons.Filled.Person,
    )
}

@Composable
fun FinNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Home
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomNavRoutes = listOf(
        Screen.SignIn.route,
        Screen.SignUp.route,
        Screen.Game.route
    )

    val shouldShowBottomNav = currentRoute !in hideBottomNavRoutes

    val items = listOf(
        Screen.Home,
        Screen.Materials,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigation(
                    backgroundColor = Color.White
                ) {
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.name) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
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
            startDestination = startDestination.route,
            Modifier.padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Lobby.route) {
                LobbyScreen(navController)
            }
            composable(Screen.Game.route) {
                GameScreen(navController)
            }
            composable(Screen.Rules.route) {
                RulesScreen(navController)
            }
            composable(Screen.Materials.route) {
                MaterialScreen(navController)
            }
            composable(Screen.Material.route) {
                DetailMaterialScreen(navController)
            }
            composable(Screen.SignIn.route) {
                SignInScreen(navController)
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
        }
    }
}
