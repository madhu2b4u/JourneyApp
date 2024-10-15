package com.divine.journey

import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.data.service.PostService
import com.divine.journey.posts.data.source.NoDataException
import com.divine.journey.posts.data.source.PostsRemoteDataSourceImpl
import com.google.gson.Gson
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InputStreamReader


// The test class
class PostsRemoteDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var service: PostService
    private lateinit var postsRepository: PostsRemoteDataSourceImpl
    private val gson = Gson()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        service = mockk()
        postsRepository = PostsRemoteDataSourceImpl(service, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    // Helper function to load JSON from resources
    private fun loadJson(filename: String): String {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("raw/$filename")
            ?: throw IllegalArgumentException("File not found: $filename")
        return InputStreamReader(inputStream).use { it.readText() }
    }

    // Helper function to create a Retrofit Response
    private fun <T> createResponse(body: T?, isSuccessful: Boolean = true): Response<T> {
        return if (isSuccessful) {
            Response.success(body)
        } else {
            Response.error(400, "Error".toResponseBody("application/json".toMediaTypeOrNull()))
        }
    }

    // Test Cases

    @Test
    fun `getPosts returns list of posts on successful response`() = runTest {
        // Arrange
        val postsJson = loadJson("posts.json")
        val posts = gson.fromJson(postsJson, Array<Post>::class.java).toList()
        coEvery { service.getPosts() } returns CompletableDeferred(createResponse(posts))

        // Act
        val result = postsRepository.getPosts()

        // Assert
        assertEquals(posts, result)
        coVerify(exactly = 1) { service.getPosts() }
    }

    @Test
    fun `getPosts throws HttpException on HTTP error response`() = runTest {
        // Arrange
        val errorResponse = CompletableDeferred(
            Response.error<List<Post>>(404, ResponseBody.create(null, "Not Found"))
        ) // Deferred Error Response
        coEvery { service.getPosts() } returns errorResponse

        // Act & Assert
        val exception = assertThrows<HttpException> {
            postsRepository.getPosts()
        }

        // Assert error code
        assertEquals(404, exception.code())

        // Verify method invocation
        coVerify(exactly = 1) { service.getPosts() }
    }


    @Test
    fun `getPosts throws NoDataException when response body is null`() = runTest {
        // Arrange
        val response = Response.success<List<Post>>(null) // Create a response with a null body
        val deferred = CompletableDeferred(response) // Wrap response in CompletableDeferred
        coEvery { service.getPosts() } returns deferred // Mock service call

        // Act & Assert
        val exception = assertThrows<NoDataException> {
            postsRepository.getPosts() // Make the actual call to the remoteDataSource
        }

        // Assert the exception message is as expected
        assertEquals("Response body is null", exception.message)

        // Verify that the service method was called exactly once
        coVerify(exactly = 1) { service.getPosts() }
    }

    @Test
    fun `getPosts throws IOException on network failure`() = runTest {
        // Arrange
        coEvery { service.getPosts() } throws IOException("Network Failure")

        // Act & Assert
        val exception = assertThrows<IOException> {
            postsRepository.getPosts()
        }

        // Assert exception message
        assertEquals("Network error occurred: Network Failure", exception.message)

        // Verify method invocation
        coVerify(exactly = 1) { service.getPosts() }
    }

    @Test
    fun `getComments returns list of comments on successful response`() = runTest {
        // Arrange
        val commentsJson = loadJson("comments.json")
        val comments = gson.fromJson(commentsJson, Array<Comment>::class.java).toList()
        val postId = 1
        coEvery { service.getComments(postId) } returns CompletableDeferred(createResponse(comments))

        // Act
        val result = postsRepository.getComments(postId)

        // Assert
        assertEquals(comments, result)
        coVerify(exactly = 1) { service.getComments(postId) }
    }

    @Test
    fun `getComments throws NoDataException when response body is null`() = runTest {
        // Arrange
        val postId = 1
        val response = Response.success<List<Comment>>(null) // Create a response with a null body
        val deferred = CompletableDeferred(response) // Wrap response in CompletableDeferred
        coEvery { service.getComments(postId) } returns deferred // Mock service call

        // Act & Assert
        val exception = assertThrows<NoDataException> {
            postsRepository.getComments(postId)
        }
        assertEquals("Response body is null", exception.message)
        coVerify(exactly = 1) { service.getComments(postId) }
    }

    @Test
    fun `getComments throws HttpException on HTTP error response`() = runTest {
        // Arrange
        val postId = 1
        val errorResponse = CompletableDeferred(
            Response.error<List<Comment>>(500, ResponseBody.create(null, "Internal Server Error"))
        )

        coEvery { service.getComments(postId) } returns errorResponse

        // Act & Assert
        val exception = assertThrows<HttpException> {
            postsRepository.getComments(postId)
        }
        // Assert error code
        assertEquals(500, exception.code())
        // Verify method invocation
        coVerify(exactly = 1) { service.getComments(postId) }
    }

    @Test
    fun `getComments throws IOException on network failure`() = runTest {
        // Arrange
        val postId = 1
        coEvery { service.getComments(postId) } throws IOException("Network Failure")

        // Act & Assert
        val exception = assertThrows<IOException> {
            postsRepository.getComments(postId)
        }

        // Assert exception message
        assertEquals("Network error occurred: Network Failure", exception.message)

        // Verify method invocation
        coVerify(exactly = 1) { service.getComments(postId) }
    }
}