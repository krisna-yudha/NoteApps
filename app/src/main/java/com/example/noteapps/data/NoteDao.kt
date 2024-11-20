package com.example.noteapps.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.noteapps.model.Note

@Dao
interface NoteDao {

    // Get all notes - suspend function to query asynchronously
    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAllNotes(): List<Note>

    // Delete all notes - suspend function to delete asynchronously, return Unit (void)
    @Query("DELETE FROM notes WHERE 1")
    suspend fun deleteAllNotes()

    // Get a single note by ID - suspend function to query asynchronously
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    // Insert a note - suspend function to insert asynchronously
    @Insert
    suspend fun insert(note: Note)

    // Update a note - suspend function to update asynchronously
    @Query("UPDATE notes SET title = :title, content = :content WHERE id = :noteId")
    suspend fun update(noteId: Int, title: String, content: String)

    // Delete a single note - suspend function to delete asynchronously
    @Delete
    suspend fun delete(note: Note)
}
