package com.my.githubapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.githubapp.data.entity.GithubEntity

@Database(entities = [GithubEntity::class], version = 1, exportSchema = false)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao
}