package com.divine.journey.posts.domain

import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post
import kotlinx.coroutines.flow.Flow

interface PostsUseCase {

    suspend fun getPosts(isLoaded: Boolean): Flow<Result<List<Post>>>

    suspend fun getComments(postId: Int): Flow<Result<List<Comment>>>

}