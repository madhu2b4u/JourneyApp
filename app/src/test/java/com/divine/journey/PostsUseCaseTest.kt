package com.divine.journey

import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.data.repository.PostsRepository
import com.divine.journey.posts.domain.PostsUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PostsUseCaseTest {

    private lateinit var postsUseCase: PostsUseCaseImpl
    private val repository = mockk<PostsRepository>()

    @Before
    fun setUp() {
        postsUseCase = PostsUseCaseImpl(repository)
    }

    @Test
    fun `getPosts calls repository and returns success result when posts are loaded`() = runTest {
        // Arrange
        val posts = listOf(Post(1, 1, "Title", "Body"))
        val expectedResult = Result.success(posts)

        coEvery { repository.getPosts(true) } returns flow { emit(expectedResult) }

        // Act
        val result = postsUseCase.getPosts(true)

        // Assert
        result.collect { res ->
            assertEquals(expectedResult, res)
        }
        coVerify(exactly = 1) { repository.getPosts(true) }
    }

    @Test
    fun `getPosts calls repository and returns error result when an exception occurs`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        val expectedResult = Result.error<List<Post>>(errorMessage, null)

        coEvery { repository.getPosts(false) } returns flow { emit(expectedResult) }

        // Act
        val result = postsUseCase.getPosts(false)

        // Assert
        result.collect { res ->
            assertEquals(expectedResult, res)
        }
        coVerify(exactly = 1) { repository.getPosts(false) }
    }

    @Test
    fun `getComments calls repository and returns success result when comments are fetched`() =
        runTest {
            // Arrange
            val comments = listOf(Comment(1, 1, "Name", "email", "Comment Body"))
            val expectedResult = Result.success(comments)

            coEvery { repository.getComments(1) } returns flow { emit(expectedResult) }

            // Act
            val result = postsUseCase.getComments(1)

            // Assert
            result.collect { res ->
                assertEquals(expectedResult, res)
            }
            coVerify(exactly = 1) { repository.getComments(1) }
        }

    @Test
    fun `getComments calls repository and returns error result when an exception occurs`() =
        runTest {
            // Arrange
            val errorMessage = "Network error"
            val expectedResult = Result.error<List<Comment>>(errorMessage, null)

            coEvery { repository.getComments(1) } returns flow { emit(expectedResult) }

            // Act
            val result = postsUseCase.getComments(1)

            // Assert
            result.collect { res ->
                assertEquals(expectedResult, res)
            }
            coVerify(exactly = 1) { repository.getComments(1) }
        }
}
