package com.binar.challenge44.Repository

import androidx.lifecycle.LiveData
import com.binar.challenge44.Database.NoteDatabase
import com.binar.challenge44.Model.User

class UserRepository(private val db: NoteDatabase) {
    suspend fun insert(user: User) = db.userDao().insert(user)

    fun getUser(email: String, password: String): LiveData<User>? {
        return db.userDao().getUser(email, password)
    }
}