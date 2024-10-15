package com.divine.journey

import com.divine.journey.database.source.LocalDataSourceImpl
import com.divine.journey.di.Status
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.data.repository.PostsRepository
import com.divine.journey.posts.data.repository.PostsRepositoryImpl
import com.divine.journey.posts.data.source.PostsRemoteDataSourceImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PostsRepositoryTest {

    private lateinit var postsRepository: PostsRepository
    private val remoteDataSource = mockk<PostsRemoteDataSourceImpl>()
    private val localDataSource = mockk<LocalDataSourceImpl>()
    private val mockPosts = listOf(Post(1, 1, "Title", "Body"))

    @Before
    fun setUp() {
        postsRepository = PostsRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getPosts returns posts from local storage when isLoaded is true`() = runTest {
        // Arrange
        coEvery { localDataSource.getPosts() } returns mockPosts

        // Act
        val flow = postsRepository.getPosts(isLoaded = true).toList()

        // Assert
        assertTrue(flow.first().status == Status.LOADING)
        assertTrue(flow.last().status == Status.SUCCESS)
        assertEquals(mockPosts, flow.last().data)
        coVerify(exactly = 1) { localDataSource.getPosts() }
        coVerify(exactly = 0) { remoteDataSource.getPosts() }
    }

    @Test
    fun `getPosts fetches from remote when local storage is empty`() = runTest {
        // Arrange
        coEvery { localDataSource.getPosts() } returns null
        coEvery { remoteDataSource.getPosts() } returns mockPosts
        coEvery { localDataSource.savePosts(mockPosts) } just Runs

        // Act
        val flow = postsRepository.getPosts(isLoaded = true).toList()

        // Assert
        assertTrue(flow.first().status == Status.LOADING)
        assertTrue(flow.last().status == Status.SUCCESS)
        assertEquals(mockPosts, flow.last().data)
        coVerify(exactly = 1) { localDataSource.getPosts() }
        coVerify(exactly = 1) { remoteDataSource.getPosts() }
        coVerify(exactly = 1) { localDataSource.savePosts(mockPosts) }
    }

    @Test
    fun `getPosts emits error when exception occurs`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { localDataSource.getPosts() } returns null
        coEvery { remoteDataSource.getPosts() } throws exception

        // Act
        val flow = postsRepository.getPosts(isLoaded = true).toList()

        // Assert
        assertTrue(flow.first().status == Status.LOADING)
        assertTrue(flow.last().status == Status.ERROR)
        assertEquals("Network error", flow.last().message)
        coVerify(exactly = 1) { localDataSource.getPosts() }
        coVerify(exactly = 1) { remoteDataSource.getPosts() }
        coVerify(exactly = 0) { localDataSource.savePosts(any()) }
    }

    @Test
    fun `getComments returns comments successfully`() = runTest {
        // Arrange
        val mockComments = listOf(Comment(1, 1, "Name", "Email", "Body"))
        coEvery { remoteDataSource.getComments(1) } returns mockComments

        // Act
        val flow = postsRepository.getComments(1).toList()

        // Assert
        assertTrue(flow.first().status == Status.LOADING)
        assertTrue(flow.last().status == Status.SUCCESS)
        assertEquals(mockComments, flow.last().data)
        coVerify(exactly = 1) { remoteDataSource.getComments(1) }
    }

    @Test
    fun `getComments emits error when exception occurs`() = runTest {
        // Arrange
        val exception = RuntimeException("Failed to fetch comments")
        coEvery { remoteDataSource.getComments(1) } throws exception

        // Act
        val flow = postsRepository.getComments(1).toList()

        // Assert
        assertTrue(flow.first().status == Status.LOADING)
        assertTrue(flow.last().status == Status.ERROR)
        assertEquals("Failed to fetch comments", flow.last().message)
        coVerify(exactly = 1) { remoteDataSource.getComments(1) }
    }


}