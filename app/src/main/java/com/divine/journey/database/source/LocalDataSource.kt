package com.divine.journey.database.source

import com.divine.journey.posts.data.model.Post

interface LocalDataSource {
    suspend fun getPosts(): List<Post>?
    suspend fun savePosts(posts: List<Post>)
}