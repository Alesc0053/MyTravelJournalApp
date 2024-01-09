package com.example.mytraveljournal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsUserViewModel : ViewModel() {
    private val _userId = MutableLiveData<Int>()
    private var username = ""
    val userId: LiveData<Int> get() = _userId

    fun setUserId(id: Int) {
        _userId.value = id
    }

    fun getUsername() : String{
        return this.username
    }

    fun setUsername(username: String) {
        this.username = username
    }
}