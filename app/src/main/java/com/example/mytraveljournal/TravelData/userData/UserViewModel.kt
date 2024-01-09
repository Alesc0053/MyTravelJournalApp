package com.example.mytraveljournal.TravelData.userData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytraveljournal.TravelData.Travel
import com.example.mytraveljournal.TravelData.TravelDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    private val repository: UserRepository
    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> get() = _loginResult


    init {
        val userDao = TravelDatabase.getInstance(application).userDao
        repository = UserRepository(userDao)
        readAllData = repository.readAllDAta
    }

    fun insertUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun loginUser(name: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.loginUser(name, password)
            _loginResult.postValue(user)
        }
    }


}