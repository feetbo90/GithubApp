package com.my.githubapp.ui.viewmodel.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.githubapp.data.entity.GithubEntity
import com.my.githubapp.data.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
    private val _favorites = MutableStateFlow(listOf<GithubEntity>())
    val favorite = _favorites.asStateFlow()

    init {
        getFavoriteUsers()
    }

    private fun getFavoriteUsers() {
        viewModelScope.launch {
            repository.getAllFavoriteUsers().collect {
                _favorites.value = it
            }
        }
    }
}