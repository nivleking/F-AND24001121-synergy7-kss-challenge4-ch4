package com.binar.challenge44

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.challenge44.Adapter.NoteAdapter
import com.binar.challenge44.Database.NoteDatabase
import com.binar.challenge44.Database.SessionManager
import com.binar.challenge44.Model.Note
import com.binar.challenge44.Repository.NoteRepository
import com.binar.challenge44.ViewModel.NoteViewModel
import com.binar.challenge44.ViewModel.NoteViewModelFactory
import com.binar.challenge44.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var sessionManager: SessionManager

    private fun setupViewModel() {
        val notes_db = NoteDatabase.getDatabase(this)
        val repository = NoteRepository(notes_db)
        val factory = NoteViewModelFactory(this.application, repository)
        notesViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
    }

    private fun addSampleNotes() {
        val note1 = Note(
            id = 0,
            title = "Sample Note 1",
            description = "This is a sample note 1"
        )
        val note2 = Note(
            id = 0,
            title = "Sample Note 2",
            description = "This is a sample note 2"
        )
        val note3 = Note(
            id = 0,
            title = "Sample Note 3",
            description = "This is a sample note 3"
        )
        notesViewModel.addNote(note1)
        notesViewModel.addNote(note2)
        notesViewModel.addNote(note3)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)
        if (!sessionManager.isUserLoggedIn()) {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupHomeRV()

        this.sessionManager = SessionManager(this)

        notesViewModel.getAllNotes().observe(this, { notes ->
            if (notes.isEmpty()) {
                addSampleNotes()
            }
        })

        binding.fabAddNote.setOnClickListener {
            binding.fabAddNote.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.dialog_view, null)
                val etTitle = dialogLayout.findViewById<EditText>(R.id.etTitle)
                val etDescription = dialogLayout.findViewById<EditText>(R.id.etDescription)

                builder.setView(dialogLayout)
                builder.setPositiveButton("Save") { dialogInterface, i ->
                    val id = 0
                    val title = etTitle.text.toString().trim()
                    val description = etDescription.text.toString().trim()

                    if (title.isEmpty() and description.isEmpty()) {
                        etTitle.error = "Title cannot be empty"
                        etDescription.error = "Description cannot be empty"

                        Toast.makeText(
                            this,
                            "Title and Description cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setPositiveButton
                    }

                    addNote(title, description)
                }
                builder.setNegativeButton("Cancel") { dialogInterface, i ->
                    // Cancel action
                }

                builder.show()
            }
        }
    }

    private fun addNote(title: String, description: String) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            val note = Note(
                id = 0,
                title = title,
                description = description
            )
            notesViewModel.addNote(note)
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(notes: List<Note>?) {
        if (notes != null) {
            if (notes.isNotEmpty()) {
                binding.tvEmptyList.visibility = View.GONE
                binding.rvNotes.visibility = View.VISIBLE
            } else {
                binding.tvEmptyList.visibility = View.VISIBLE
                binding.rvNotes.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRV() {
        noteAdapter = NoteAdapter(notesViewModel)
        binding.rvNotes.adapter = noteAdapter
        //vertical layoutmanager
        binding.rvNotes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        notesViewModel.getAllNotes().observe(this, { notes ->
            noteAdapter.differ.submitList(notes)
            updateUI(notes)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutBtn -> {
                sessionManager.logout()
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}