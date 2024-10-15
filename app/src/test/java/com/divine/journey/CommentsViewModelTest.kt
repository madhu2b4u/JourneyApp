package com.divine.journey

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.domain.PostsUseCase
import com.divine.journey.posts.presentation.viewmodel.CommentsViewModel
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
class CommentsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var postsUseCase: PostsUseCase
    private lateinit var viewModel: CommentsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        postsUseCase = mockk()
        viewModel = CommentsViewModel(postsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchComments should update state to loading then success`() = testScope.runTest {
        // Given
        val postId = 1
        val comments = listOf(Comment(1, postId, "Name", "Email", "Body"))
        coEvery { postsUseCase.getComments(postId) } returns flowOf(
            Result.loading(),
            Result.success(comments)
        )

        // When
        viewModel.fetchComments(postId)

        // Then
        assertEquals(Result.loading<List<Comment>>(), viewModel.commentsState.value)
        advanceUntilIdle()
        assertEquals(Result.success(comments), viewModel.commentsState.value)
    }

    @Test
    fun `fetchComments should handle error`() = testScope.runTest {
        // Given
        val postId = 1
        val errorMessage = "Failed to fetch comments"
        coEvery { postsUseCase.getComments(postId) } returns flowOf(
            Result.loading(),
            Result.error(errorMessage)
        )

        // When
        viewModel.fetchComments(postId)

        // Then
        assertEquals(Result.loading<List<Comment>>(), viewModel.commentsState.value)
        advanceUntilIdle()
        assertEquals(Result.error<List<Comment>>(errorMessage), viewModel.commentsState.value)
    }

    /* @Test
     fun `clearCommentsState should reset state to loading`() = testScope.runTest {
         // Given
         val comments = listOf(Comment(1, 1, "Name", "Email", "Body"))
         viewModel.commentsState.value = Result.success(comments)

         // When
         viewModel.clearCommentsState()

         // Then
         assertEquals(Result.loading<List<Comment>>(), viewModel.commentsState.value)
     }*/
}
