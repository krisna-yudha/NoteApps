package com.example.noteapps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.noteapps.data.NoteDao
import com.example.noteapps.data.NoteDatabase
import com.example.noteapps.model.Note
import com.example.noteapps.repository.NoteRepository
import com.example.noteapps.viewmodel.NoteViewModel
import com.google.gson.Gson

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var noteViewModel: NoteViewModel
    private var existingNote: Note? = null // Untuk menyimpan catatan yang diedit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        // Inisialisasi Room dan ViewModel
        val noteDao: NoteDao = NoteDatabase.getDatabase(application).noteDao()
        val noteRepository = NoteRepository(noteDao)
        val factory = NoteViewModelFactory(noteRepository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)

        // Inisialisasi Views
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        // Cek apakah ada data catatan untuk diedit
        val noteJson = intent.getStringExtra("note")
        if (noteJson != null) {
            existingNote = Gson().fromJson(noteJson, Note::class.java)
            etTitle.setText(existingNote?.title)
            etContent.setText(existingNote?.content)
        }

        // Tombol Simpan
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val content = etContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah ini catatan baru atau update
            if (existingNote != null) {
                // Update catatan
                val updatedNote = existingNote!!.copy(title = title, content = content)
                noteViewModel.updateNote(updatedNote)
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()

                // Kembalikan hasil ke Activity asal
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("note", Gson().toJson(updatedNote))
                })
            } else {
                // Tambahkan catatan baru
                val newNote = Note(title = title, content = content)
                noteViewModel.insertnote(newNote)
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        // Tombol Batal
        btnCancel.setOnClickListener {
            finish() // Tutup Activity tanpa menyimpan
        }
    }
}
