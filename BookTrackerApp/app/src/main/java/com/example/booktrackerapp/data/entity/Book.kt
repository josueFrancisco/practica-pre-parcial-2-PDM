package com.example.booktrackerapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("books")
data class Books (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val synopsis: String
)
