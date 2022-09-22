package com.my.githubapp.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.githubapp.data.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val githubRepository: GithubRepository) :
    ViewModel() {

    val getThemeSetting: Flow<Boolean> = githubRepository.getThemeSetting()

    fun saveThemeSetting(darkModeState: Boolean) {
        viewModelScope.launch {
            githubRepository.saveThemeSetting(darkModeState)
        }
    }
}