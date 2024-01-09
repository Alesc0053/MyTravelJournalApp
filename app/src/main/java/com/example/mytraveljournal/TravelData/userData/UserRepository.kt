package com.example.mytraveljournal.TravelData.userData

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val readAllDAta: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun loginUser(name: String, password: String): User? {
        return userDao.loginUser(name, password)
    }

}