package com.example.booktrackerapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = Books::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            //Se recomienda restrict en lugar de cascade
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId")]
)

data class BookNotes (
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    val bookId: Int,
    val content: String,
    // Fecha de creación en milisegundos
    val dateTimestamp: Long
)