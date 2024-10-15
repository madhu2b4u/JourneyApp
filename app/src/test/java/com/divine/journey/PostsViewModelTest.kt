package com.divine.journey

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.domain.PostsUseCase
import com.divine.journey.posts.presentation.viewmodel.PostsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class PostsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var postsUseCase: PostsUseCase
    private lateinit var viewModel: PostsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        postsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch posts`() = testScope.runTest {
        // Given
        val posts = listOf(Post(1, 1, "Title", "Body"))
        coEvery { postsUseCase.getPosts(false) } returns flowOf(Result.success(posts))

        // When
        viewModel = PostsViewModel(postsUseCase)

        // Then
        advanceUntilIdle()
        assertEquals(Result.success(posts), viewModel.posts.value)
    }

    @Test
    fun `fetchPosts should update state to loading then success`() = testScope.runTest {
        // Given
        val posts = listOf(Post(1, 1, "Title", "Body"))
        coEvery { postsUseCase.getPosts(true) } returns flowOf(
            Result.loading(),
            Result.success(posts)
        )
        viewModel = PostsViewModel(postsUseCase)

        // When
        viewModel.fetchPosts(true)

        // Then
        assertEquals(Result.loading<List<Post>>(), viewModel.posts.value)
        advanceUntilIdle()
        assertEquals(Result.success(posts), viewModel.posts.value)
    }

    @Test
    fun `fetchPosts should handle error`() = testScope.runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { postsUseCase.getPosts(false) } returns flowOf(Result.error(errorMessage))

        viewModel = PostsViewModel(postsUseCase)

        // When
        viewModel.fetchPosts(false)

        // Then
        advanceUntilIdle()
        assertEquals(Result.error<List<Post>>(errorMessage), viewModel.posts.value)
    }
}
