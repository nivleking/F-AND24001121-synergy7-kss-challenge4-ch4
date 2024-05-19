package com.binar.challenge44.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.binar.challenge44.Model.User
import com.binar.challenge44.Repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(app: Application, private val repository: UserRepository) :
    AndroidViewModel(app) {
    fun addUser(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    fun getUser(email: String, password: String): LiveData<User>? {
        return repository.getUser(email, password)
    }
}
