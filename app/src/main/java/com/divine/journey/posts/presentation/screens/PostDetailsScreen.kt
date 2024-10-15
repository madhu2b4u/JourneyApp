package com.divine.journey.posts.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.divine.journey.R
import com.divine.journey.di.Status
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.presentation.viewmodel.CommentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(postId: Int, postBody: String, navController: NavController) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val uiState by viewModel.commentsState.collectAsState()

    LaunchedEffect(postId) {
        viewModel.fetchComments(postId)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(stringResource(R.string.post_details)) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearCommentsState()
                        navController.navigateUp()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {


                Text(
                    text = postBody,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp // Set font size to 24sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = stringResource(R.string.comments),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp // Set font size to 24sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )


                when (uiState.status) {
                    Status.LOADING -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    Status.SUCCESS -> {
                        val comments = uiState.data ?: emptyList()
                        if (comments.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()


                            ) {
                                itemsIndexed(comments) { index, comment ->
                                    CommentItem(comment)
                                    if (index < comments.size - 1) {
                                        HorizontalDivider(
                                            color = Color.Black,
                                            thickness = 0.5.dp,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No comments available", color = Color.Gray)
                            }
                        }
                    }

                    Status.ERROR -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: ${uiState.message}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Column(modifier = Modifier.padding(0.dp)) {
        Text(
            text = comment.body, style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Light,
                fontSize = 14.sp // Set font size to 24sp
            )
        )
    }
}
