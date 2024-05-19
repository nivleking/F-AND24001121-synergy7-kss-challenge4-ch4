package com.binar.challenge44.Repository

import com.binar.challenge44.Database.NoteDatabase
import com.binar.challenge44.Model.Note

class NoteRepository(private val db: NoteDatabase) {
    suspend fun insert(note: Note) = db.noteDao().insert(note)
    suspend fun update(note: Note) = db.noteDao().update(note)
    suspend fun delete(note: Note) = db.noteDao().delete(note)

    fun getAllNotes() = db.noteDao().getAllNotes()
}