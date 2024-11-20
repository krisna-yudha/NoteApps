package com.example.noteapps.repository

import androidx.lifecycle.LiveData
import com.example.noteapps.data.NoteDao
import com.example.noteapps.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    // Function to get all notes
    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }

    // Function to delete all notes
    suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()  // Just call DAO's suspend delete function
    }

    // Function to get a note by ID
    suspend fun getNoteById(noteId: Int): Note? {
        return noteDao.getNoteById(noteId)
    }

    // Function to insert a note
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note.id, note.title, note.content)
    }

    // Function to delete a single note
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}
