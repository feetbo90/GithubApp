package com.my.githubapp.data.network

import com.my.githubapp.BuildConfig
import com.my.githubapp.data.ResponseSearch
import com.my.githubapp.data.SimpleUser
import com.my.githubapp.data.User
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("search/users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getUserList(@Query("q") q: String): ResponseSearch

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getDetailUser(@Path("username") username: String): User

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getFollowersList(@Path("username") username: String): ArrayList<SimpleUser>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getFollowingList(@Path("username") username: String): ArrayList<SimpleUser>

    companion object {
        private const val API_TOKEN = BuildConfig.API_TOKEN
    }
}