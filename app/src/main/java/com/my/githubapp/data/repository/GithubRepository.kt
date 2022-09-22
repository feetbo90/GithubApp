package com.my.githubapp.data.repository

import android.util.Log
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.User
import com.my.githubapp.data.db.GithubDao
import com.my.githubapp.data.entity.GithubEntity
import com.my.githubapp.data.network.ApiServices
import com.my.githubapp.data.preferences.MyPreferences
import com.my.githubapp.data.result.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val githubDao: GithubDao,
    private val myPreferences: MyPreferences
) {
    fun searchUser(q: String): Flow<MyResult<ArrayList<SimpleUser>>> = flow {
        emit(MyResult.Loading)
        try {
            val users = apiServices.getUserList(q).items
            emit(MyResult.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "searchUserByUsername: ${e.message.toString()}")
            emit(MyResult.Error(e.message.toString()))
        }
    }

    fun getDetailUser(username: String): Flow<MyResult<User>> = flow {
        emit(MyResult.Loading)
        try {
            val user = apiServices.getDetailUser(username)
            emit(MyResult.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "getUserDetail: ${e.message.toString()}")
            emit(MyResult.Error(e.message.toString()))
        }
    }

    fun getUserFollowers(id: String): Flow<MyResult<ArrayList<SimpleUser>>> = flow {
        emit(MyResult.Loading)
        try {
            val users = apiServices.getFollowersList(id)
            emit(MyResult.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowers: ${e.message.toString()}")
            emit(MyResult.Error(e.message.toString()))
        }
    }

    fun getUserFollowing(id: String): Flow<MyResult<ArrayList<SimpleUser>>> = flow {
        emit(MyResult.Loading)
        try {
            val users = apiServices.getFollowingList(id)
            emit(MyResult.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowing: ${e.message.toString()}")
            emit(MyResult.Error(e.message.toString()))
        }
    }

    fun getThemeSetting(): Flow<Boolean> = myPreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        myPreferences.saveThemeSetting(isDarkModeActive)
    }

    suspend fun saveUserAsFavorite(user: GithubEntity) {
        githubDao.insert(user)
    }

    suspend fun deleteFromFavorite(user: GithubEntity) {
        githubDao.delete(user)
    }

    fun getAllFavoriteUsers(): Flow<List<GithubEntity>> = githubDao.getAllUsers()

    fun isFavoriteUser(id: String): Flow<Boolean> = githubDao.isFavoriteUser(id)

    companion object {
        private val TAG = GithubRepository::class.java.simpleName
    }
}