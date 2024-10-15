package com.divine.journey.navigation


sealed class NavigationItem(val route: String) {
    data object Post : NavigationItem("post")
    data object PostDetails : NavigationItem("postDetails/{postId}")
}