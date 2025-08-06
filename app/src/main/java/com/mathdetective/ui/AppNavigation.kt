package com.mathdetective.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mathdetective.ui.home.HomeScreen
import com.mathdetective.ui.practice.PracticeScreen
import com.mathdetective.ui.report.ReportScreen
import com.mathdetective.ui.rewards.RewardsScreen

// Definición de las rutas y configuración de navegación
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Game : Screen("practice", "Jugar", Icons.Default.PlayArrow)
    data object Progress : Screen("report", "Mi Progreso", Icons.Default.Analytics)
    data object Rewards : Screen("rewards", "Mis Recompensas", Icons.Default.EmojiEvents)
}

val bottomNavItems = listOf(
    Screen.Game,
    Screen.Progress,
    Screen.Rewards
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determinar si mostrar la navegación (no mostrar en home)
    val showTopAndBottomNav = currentDestination?.route != "home"

    Scaffold(
        topBar = {
            if (showTopAndBottomNav) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Math Detective 🕵️",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Ir al inicio",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF5E72E4),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            if (showTopAndBottomNav) {
                NavigationBar(
                    containerColor = Color(0xFF5E72E4), // Color azul del tema de tu app
                    contentColor = Color.White
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.title,
                                    tint = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true)
                                        Color.White else Color.White.copy(alpha = 0.6f)
                                )
                            },
                            label = {
                                Text(
                                    screen.title,
                                    color = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true)
                                        Color.White else Color.White.copy(alpha = 0.6f)
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                selectedTextColor = Color.White,
                                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                indicatorColor = Color.White.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onPlayClick = { navController.navigate("practice") },
                    onProgressClick = { navController.navigate("report") },
                    onRewardsClick = { navController.navigate("rewards") }
                )
            }
            composable("practice") {
                PracticeScreen()
            }
            composable("report") {
                ReportScreen()
            }
            composable("rewards") {
                RewardsScreen()
            }
        }
    }
}