package com.my.githubapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.repository.GithubRepository
import com.my.githubapp.data.result.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: GithubRepository): ViewModel(){

    val themeSetting: Flow<Boolean> = repository.getThemeSetting()
    private val _users = MutableStateFlow<MyResult<ArrayList<SimpleUser>>>(MyResult.Loading)
    val users = _users.asStateFlow()

    init {
        searchUser("\"\"")
    }

    fun searchUser(query: String) {
        _users.value = MyResult.Loading
        viewModelScope.launch {
            repository.searchUser(query).collect {
                _users.value = it
            }
        }
    }
}