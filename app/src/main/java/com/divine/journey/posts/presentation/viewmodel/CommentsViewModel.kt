package com.divine.journey.posts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Comment
import com.divine.journey.posts.domain.PostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val postsUseCase: PostsUseCase
) : ViewModel() {

    private val _commentsState = MutableStateFlow<Result<List<Comment>>>(Result.loading())
    val commentsState: StateFlow<Result<List<Comment>>> = _commentsState.asStateFlow()

    fun fetchComments(postId: Int) {
        viewModelScope.launch {
            postsUseCase.getComments(postId)
                .onStart { _commentsState.value = Result.loading() }
                .collect { result ->
                    _commentsState.value = result
                }
        }
    }

    fun clearCommentsState() {
        _commentsState.value = Result.loading()
    }
}
