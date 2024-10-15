package com.divine.journey.posts.data.repository

import com.divine.journey.database.source.LocalDataSourceImpl
import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.data.source.PostsRemoteDataSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val remoteDataSource: PostsRemoteDataSourceImpl,
    private val localDataSource: LocalDataSourceImpl,
) : PostsRepository {

    override suspend fun getPosts(isLoaded: Boolean): Flow<Result<List<Post>>> = flow {
        emit(Result.loading())
        try {
            var posts: List<Post>? = null
            if (isLoaded)
                posts = localDataSource.getPosts()

            if (posts == null) {
                posts = remoteDataSource.getPosts()
                localDataSource.savePosts(posts)
            }
            emit(Result.success(posts))
        } catch (exception: Exception) {
            // If an exception occurs, emit error state
            emit(Result.error(exception.message ?: "", null))
        }
    }

    override suspend fun getComments(postId: Int): Flow<Result<List<Comment>>> = flow {
        emit(Result.loading())
        try {
            val comments = remoteDataSource.getComments(postId)
            emit(Result.success(comments))
        } catch (exception: Exception) {
            // If an exception occurs, emit error state
            emit(Result.error(exception.message ?: "", null))
        }
    }


}
