package com.my.githubapp.dependencies

import com.my.githubapp.data.network.ApiConfig
import com.my.githubapp.data.network.ApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiDi {
    @Provides
    @Singleton
    fun provideApiServices(): ApiServices = ApiConfig.getApiService()
}