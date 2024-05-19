package com.binar.challenge44.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.binar.challenge44.Model.Note
import com.binar.challenge44.R
import com.binar.challenge44.ViewModel.NoteViewModel
import com.binar.challenge44.databinding.ListItemBinding

class NoteAdapter(
    private val notesViewModel: NoteViewModel
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(val itemBinding: ListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.description == newItem.description
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curNote = differ.currentList[position]

        holder.itemBinding.noteTitle.text = curNote.title
        holder.itemBinding.noteDescription.text = curNote.description

        holder.itemBinding.delBtn.setOnClickListener {
            notesViewModel.delete(curNote)
            Toast.makeText(holder.itemView.context, "Note deleted", Toast.LENGTH_SHORT).show()
        }

        holder.itemBinding.editBtn.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogLayout = inflater.inflate(R.layout.dialog_view, null)
            val etTitle = dialogLayout.findViewById<EditText>(R.id.etTitle)
            val etDescription = dialogLayout.findViewById<EditText>(R.id.etDescription)

            etTitle.setText(curNote.title)
            etDescription.setText(curNote.description)

            builder.setView(dialogLayout)
            builder.setPositiveButton("Update") { dialogInterface, i ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()

                if (title.isEmpty() and description.isEmpty()) {
                    etTitle.error = "Title cannot be empty"
                    etDescription.error = "Description cannot be empty"

                    Toast.makeText(
                        holder.itemView.context,
                        "Title and Description cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setPositiveButton
                }

                val updatedNote = Note(
                    id = curNote.id,
                    title = title,
                    description = description
                )
                notesViewModel.update(updatedNote)
                Toast.makeText(holder.itemView.context, "Note updated", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialogInterface, i ->
                // Cancel action
            }
            builder.show()
        }
    }
}