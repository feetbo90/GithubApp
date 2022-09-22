package com.my.githubapp.dependencies

import android.content.Context
import androidx.room.Room
import com.my.githubapp.data.db.GithubDao
import com.my.githubapp.data.db.GithubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModul {

    @Provides
    fun provideUserDao(userDatabase: GithubDatabase): GithubDao = userDatabase.githubDao()

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): GithubDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            GithubDatabase::class.java,
            "note_database"
        ).build()
    }
}