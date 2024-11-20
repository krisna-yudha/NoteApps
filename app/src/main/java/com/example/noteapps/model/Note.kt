package com.example.noteapps.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "title") // Custom column name (optional)
    val title: String,

    @ColumnInfo(name = "content") // Custom column name (optional)
    val content: String,

    @ColumnInfo(name = "timestamp") // Stores Date as a Long with TypeConverter
    val timestamp: Long = System.currentTimeMillis() // Default to current timestamp
)

