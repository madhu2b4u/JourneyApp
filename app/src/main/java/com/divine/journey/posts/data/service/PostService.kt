package com.divine.journey.posts.data.service

import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//Service interface for handling posts-related API calls.

interface PostService {
    /**
     * Retrieves a list of posts from the server.
     *
     * @return A Deferred Response containing a List of post objects.
     *         The use of Deferred allows for asynchronous handling of the network request.
     * @GET annotation specifies this is a GET request to the "post" endpoint.
     */
    @GET("posts")
    fun getPosts(): Deferred<Response<List<Post>>>

    /**
     * Retrieves a list of comments associated with post id from the server.
     *
     * @return A Deferred Response containing a List of comment objects.
     *         The use of Deferred allows for asynchronous handling of the network request.
     * @GET annotation specifies this is a GET request to the "comments" endpoint.
     */

    @GET("posts/{postId}/comments")
    fun getComments(@Path("postId") postId: Int): Deferred<Response<List<Comment>>>
}