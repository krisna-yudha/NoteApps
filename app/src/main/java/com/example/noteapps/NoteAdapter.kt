package com.example.noteapps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapps.model.Note

class NoteAdapter(private val listener: OnItemClickListener) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    // ViewHolder untuk item catatan
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            // Klik pada item untuk edit
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }

            // Klik tombol delete untuk menghapus
            deleteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(getItem(position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.title.text = currentNote.title
    }

    // DiffUtil untuk membandingkan data
    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    // Interface untuk event klik
    interface OnItemClickListener {
        fun onItemClick(note: Note)       // Untuk edit
        fun onDeleteClick(note: Note)    // Untuk hapus
    }
}
