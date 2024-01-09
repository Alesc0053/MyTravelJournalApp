package com.example.mytraveljournal.TravelData.userData

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE name = :name AND password = :password")
    suspend fun loginUser(name: String, password: String): User?

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}