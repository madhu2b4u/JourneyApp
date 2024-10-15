package com.divine.journey.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.divine.journey.posts.presentation.screens.PostDetailsScreen
import com.divine.journey.posts.presentation.screens.PostListScreen

/**
 * Composable function that sets up the navigation graph for the application.
 *
 * This function creates a [NavHost] and defines the routes and composable
 * destinations for the app's navigation. It specifies the starting destination
 * and handles navigation between different screens.
 *
 * @param navController The [NavHostController] used to control navigation.
 * @param startDestination The route of the initial screen to display.
 * Defaults to [NavigationItem.Post.route].
 */

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
            val postBody = backStackEntry.arguments?.getString("postBody") ?: ""
            PostDetailsScreen(postId, postBody, navController)
        }
    }
}

/**
 * Represents the different screens in the application's navigation graph.
 *
 * This sealed class defines the routes for each screen and provides a way
 * to navigate between them. Each object within the sealed class represents
 * a specific screen.
 */

sealed class NavigationItem(val route: String) {
    /**
    * Represents the Post List screen.
    */
    data object Post : NavigationItem("post")
    /**
     * Represents the Post Details screen.
     *
     * This screen expects two arguments: `postId` and `postBody`.
     * The `createRoute` function helps in constructing the route
     * with these arguments.
     */
    data object PostDetails : NavigationItem("postDetails/{postId}/{postBody}") {
        /**
         * Creates the route for the Post Details screen with the given arguments.
         *
         * @param postId The ID of the post to display.
         * @param postBody The body content of the post.
         * @return The route string for the Post Details screen.
         */
        fun createRoute(postId: Int, postBody: String): String {
            return "postDetails/$postId/${Uri.encode(postBody)}"
        }
    }
}
