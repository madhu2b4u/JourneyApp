package com.divine.journey.posts.data.source

import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post

interface PostsRemoteDataSource {
    suspend fun getPosts(): List<Post>
    suspend fun getComments(postId: Int): List<Comment>
}