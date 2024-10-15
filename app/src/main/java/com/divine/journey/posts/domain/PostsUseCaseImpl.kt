package com.divine.journey.posts.domain

import com.divine.journey.posts.data.repository.PostsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsUseCaseImpl @Inject constructor(private val repository: PostsRepository) :
    PostsUseCase {
    override suspend fun getPosts(isLoaded: Boolean) = repository.getPosts(isLoaded)

    override suspend fun getComments(postId: Int) = repository.getComments(postId)

}