package com.my.githubapp.data.db

import androidx.room.*
import com.my.githubapp.data.entity.GithubEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: GithubEntity)

    @Update
    suspend fun update(userEntity: GithubEntity)

    @Delete
    suspend fun delete(userEntity: GithubEntity)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getAllUsers(): Flow<List<GithubEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id AND is_favorite = 1)")
    fun isFavoriteUser(id: String): Flow<Boolean>
}