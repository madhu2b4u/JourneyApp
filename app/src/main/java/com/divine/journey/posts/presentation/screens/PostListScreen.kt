package com.divine.journey.posts.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.divine.journey.R
import com.divine.journey.di.Status
import com.divine.journey.navigation.NavigationItem
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.presentation.viewmodel.PostsViewModel

@Composable
fun PostListScreen(navController: NavController) {

    val viewModel: PostsViewModel = hiltViewModel()

    val postsState by viewModel.posts.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredPosts by viewModel.filteredPosts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPosts(isLoaded = false)
    }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { newText -> viewModel.searchQuery.value = newText },
            placeholder = { Text(stringResource(R.string.search_posts)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        when (postsState.status) {
            Status.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            Status.SUCCESS -> {
                val posts = filteredPosts

                LazyColumn {
                    items(
                        count = posts.size,
                        contentType = { index -> "posts" }
                    ) { index ->
                        val post = posts[index]
                        PostItem(post = post) {
                            navController.navigate(
                                NavigationItem.PostDetails.createRoute(
                                    post.id,
                                    post.title
                                )
                            )
                        }
                    }
                }
            }

            Status.ERROR -> {
                Text(text = "Error: ${postsState.message}", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun PostItem(post: Post, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
            Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
