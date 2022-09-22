package com.my.githubapp.ui.viewmodel.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.githubapp.data.User
import com.my.githubapp.data.entity.GithubEntity
import com.my.githubapp.data.repository.GithubRepository
import com.my.githubapp.data.result.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: GithubRepository): ViewModel(){

    private val _userDetail = MutableStateFlow<MyResult<User>>(MyResult.Loading)
    val userDetail = _userDetail.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    fun getDetails(username: String) {
        _userDetail.value = MyResult.Loading
        viewModelScope.launch {
            repository.getDetailUser(username).collect {
                _userDetail.value = it
            }
        }

        _isLoaded.value = true
    }

    fun saveAsFavorite(user: GithubEntity) {
        viewModelScope.launch {
            repository.saveUserAsFavorite(user)
        }
    }

    fun deleteFromFavorite(user: GithubEntity) {
        viewModelScope.launch {
            repository.deleteFromFavorite(user)
        }
    }

    fun isFavoriteGithub(id: String): Flow<Boolean> = repository.isFavoriteUser(id)
}