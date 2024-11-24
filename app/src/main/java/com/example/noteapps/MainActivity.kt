package com.example.noteapps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapps.viewmodel.NoteViewModel
import com.example.noteapps.data.NoteDao
import com.example.noteapps.data.NoteDatabase
import com.example.noteapps.model.Note
import com.example.noteapps.repository.NoteRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteRepository: NoteRepository
    private lateinit var noteDao: NoteDao
    private lateinit var themeSwitch: Switch // Switch untuk toggle tema

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the NoteDao and NoteRepository
        noteDao = NoteDatabase.getDatabase(application).noteDao()
        noteRepository = NoteRepository(noteDao)

        // Initialize the ViewModel with the NoteRepository
        val factory = NoteViewModelFactory(noteRepository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        noteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the theme toggle switch
        themeSwitch = findViewById(R.id.switch_theme)

        // Set initial switch state based on current theme
        themeSwitch.isChecked = isDarkModeEnabled()

        // Observe changes in the note list
        noteViewModel.allNotes.observe(this, { notes ->
            notes?.let {
                noteAdapter.submitList(it) // Use submitList to update the RecyclerView
            }
        })

        // Handle FAB click to navigate to another screen (e.g., add note)
        fab.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }

        // Set listener for theme toggle switch
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleTheme(isChecked)
        }
    }

    // Fungsi untuk toggle tema berdasarkan switch
    private fun toggleTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) // Dark Mode
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Light Mode
        }
        recreate() // Apply theme change
    }

    // Cek apakah Dark Mode sedang aktif
    private fun isDarkModeEnabled(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Memuat menu toggle tema
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                toggleTheme(!isDarkModeEnabled()) // Toggle tema jika menu di-click
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(note: Note) {
        editNote(note)
    }

    override fun onDeleteClick(note: Note) {
        // Handle delete button click
        noteViewModel.deleteNote(note) // Delete note from ViewModel
        Toast.makeText(this, "Deleted: ${note.title}", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.getAllNotes()
    }

    private fun editNote(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java)
        val noteJson = Gson().toJson(note)
        intent.putExtra("note", noteJson)
        startActivityForResult(intent, REQUEST_EDIT_NOTE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT_NOTE && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("note")?.let { noteJson ->
                val updatedNote = Gson().fromJson(noteJson, Note::class.java)
                noteViewModel.updateNote(updatedNote)
                noteViewModel.getAllNotes()
            }
        }
    }

    companion object {
        const val REQUEST_EDIT_NOTE = 1
    }
}

class NoteViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            NoteViewModel(noteRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
