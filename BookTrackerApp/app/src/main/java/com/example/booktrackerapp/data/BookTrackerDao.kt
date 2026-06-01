package com.example.booktrackerapp.data

import androidx.room.*
import com.example.booktrackerapp.data.entity.Books
import com.example.booktrackerapp.data.entity.BookNotes
import com.example.booktrackerapp.data.entity.BookWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface BookTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(books: Books)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookNote(note: BookNotes)

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Books>>

    @Transaction
    @Query("SELECT * FROM books")
    fun getBooksWithNotes(): Flow<List<BookWithNotes>>

    @Query("SELECT * FROM notes WHERE bookId = :bookId")
    fun getNotesByBook(bookId: Int): Flow<List<BookNotes>>

    @Delete
    suspend fun deleteBook(books: Books)

    @Update
    suspend fun updateBook(books: Books)

    @Update
    suspend fun updateBookNote(note: BookNotes)

    @Delete
    suspend fun deleteBookNote(note: BookNotes)
}