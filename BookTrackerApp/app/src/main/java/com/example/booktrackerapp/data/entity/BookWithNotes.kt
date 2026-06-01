package com.example.booktrackerapp.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookWithNotes(
    @Embedded val books: Books,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val notesList: List<BookNotes>
)