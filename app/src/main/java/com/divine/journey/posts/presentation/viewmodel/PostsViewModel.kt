package com.divine.journey.posts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divine.journey.di.Result
import com.divine.journey.posts.data.model.Post
import com.divine.journey.posts.domain.PostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsUseCase: PostsUseCase
) : ViewModel() {

    private val _postsState = MutableStateFlow<Result<List<Post>>>(Result.loading())
    val postsState: StateFlow<Result<List<Post>>> = _postsState.asStateFlow()

    init {
        fetchPosts(false)
    }

    fun fetchPosts(isLoaded: Boolean) {
        viewModelScope.launch {
            try {
                postsUseCase.getPosts(isLoaded)
                    .onStart { _postsState.value = Result.loading() }
                    .collect { result ->
                        _postsState.value = result
                    }
            } catch (e: Exception) {
                _postsState.value = Result.error("Failed to fetch posts: ${e.message}")
            }
        }
    }
}
