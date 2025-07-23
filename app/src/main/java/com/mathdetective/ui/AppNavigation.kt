package com.mathdetective.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mathdetective.ui.home.HomeScreen
import com.mathdetective.ui.practice.PracticeScreen
import com.mathdetective.ui.report.ReportScreen
import com.mathdetective.ui.rewards.RewardsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
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