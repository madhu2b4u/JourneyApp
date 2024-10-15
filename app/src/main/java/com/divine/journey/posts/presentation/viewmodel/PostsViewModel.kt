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

    private val _posts = MutableStateFlow<Result<List<Post>>>(Result.loading())
    val posts: StateFlow<Result<List<Post>>> = _posts.asStateFlow()

    private val _filteredPosts = MutableStateFlow<List<Post>>(emptyList())
    val filteredPosts: StateFlow<List<Post>> = _filteredPosts.asStateFlow()

    var searchQuery = MutableStateFlow("")

    init {
        fetchPosts(false)
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery.collect { query ->
                val currentPosts = _posts.value.data ?: emptyList()
                _filteredPosts.value = if (query.isEmpty()) {
                    currentPosts
                } else {
                    currentPosts.filter { post ->
                        post.title.contains(query, ignoreCase = true)
                    }
                }
            }
        }
    }

    fun fetchPosts(isLoaded: Boolean) {
        viewModelScope.launch {
            try {
                postsUseCase.getPosts(isLoaded)
                    .onStart { _posts.value = Result.loading() }
                    .collect { result ->
                        _posts.value = result
                        _filteredPosts.value = result.data ?: emptyList()
                    }
            } catch (e: Exception) {
                _posts.value = Result.error("Failed to fetch posts: ${e.message}")
            }
        }
    }
}
