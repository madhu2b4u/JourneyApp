package com.divine.journey.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun JourneyAppNavigation(navController: NavHostController) {
    MaterialTheme {
        AppNavHost(navController)
    }
}