package com.example.noteapps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapps.data.NoteDatabase
import com.example.noteapps.model.Note
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapps.repository.NoteRepository

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private val _allNotes: MutableLiveData<List<Note>> = MutableLiveData()
    val allNotes: LiveData<List<Note>> get() = _allNotes

    private val _deleteStatus: MutableLiveData<Boolean> = MutableLiveData()
    val deleteStatus: LiveData<Boolean> get() = _deleteStatus

    // Function to get all notes
    fun getAllNotes() {
        viewModelScope.launch {
            _allNotes.value = noteRepository.getAllNotes()
        }
    }

    // Function to delete all notes
    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
            // Reload notes after deletion
            _allNotes.value = noteRepository.getAllNotes()
        }
    }

    fun insertnote(note: Note) {
        viewModelScope.launch {
            noteRepository.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note) // Hapus catatan spesifik
            _allNotes.value = noteRepository.getAllNotes() // Refresh daftar catatan
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }
}