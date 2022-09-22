package com.my.githubapp.ui.viewmodel.follows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.repository.GithubRepository
import com.my.githubapp.data.result.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor(private val repository: GithubRepository): ViewModel() {
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    private val _followers = MutableStateFlow<MyResult<ArrayList<SimpleUser>>>(MyResult.Loading)
    val followers = _followers.asStateFlow()

    private val _following = MutableStateFlow<MyResult<ArrayList<SimpleUser>>>(MyResult.Loading)
    val following = _following.asStateFlow()


    fun getUserFollowers(username: String) {
        _followers.value = MyResult.Loading
        viewModelScope.launch {
            repository.getUserFollowers(username).collect {
                _followers.value = it
            }
        }
        _isLoaded.value = true
    }

    fun getUserFollowing(username: String) {
        _following.value = MyResult.Loading
        viewModelScope.launch {
            repository.getUserFollowing(username).catch { e ->
                _following.value = MyResult.Error(e.message.toString())
            }.collect {
                _following.value = it
            }
        }
        _isLoaded.value = true
    }
}