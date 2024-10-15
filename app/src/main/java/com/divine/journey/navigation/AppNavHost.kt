package com.divine.journey.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.divine.journey.posts.presentation.screens.PostDetailsScreen
import com.divine.journey.posts.presentation.screens.PostListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.Post.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Post.route) {
            PostListScreen(navController)
        }

        composable(NavigationItem.PostDetails.route) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")?.toInt() ?: 0
            PostDetailsScreen(postId, navController)
        }
    }
}

