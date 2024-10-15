package com.divine.journey.posts.data.source

import com.divine.journey.di.qualifiers.IO
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.service.PostService
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PostsRemoteDataSourceImpl @Inject constructor(
    private val service: PostService,
    @IO private val context: CoroutineContext
) : PostsRemoteDataSource {

    override suspend fun getPosts() = withContext(context) {
       try {
           val response = service.getPosts().await()
           if (response.isSuccessful) {
               response.body() ?: throw NoDataException("Response body is null")
           } else {
               throw HttpException(response)
           }
       }catch (e: IOException) {
           throw IOException("Network error occurred: ${e.message}", e)
       }
    }

    override suspend fun getComments(postId: Int)= withContext(context) {
        try {
            val response = service.getComments(postId).await()
            if (response.isSuccessful) {
                response.body() ?: throw NoDataException("Response body is null")
            } else {
                throw HttpException(response)
            }
        }catch (e: IOException) {
            throw IOException("Network error occurred: ${e.message}", e)
        }
    }

}

//Custom exception for when no data is returned in a successful response.

class NoDataException(message: String) : Exception(message)
