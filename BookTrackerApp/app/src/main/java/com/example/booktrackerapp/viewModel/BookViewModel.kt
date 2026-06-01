package com.example.booktrackerapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktrackerapp.data.AppDatabase
import com.example.booktrackerapp.data.entity.Books
import com.example.booktrackerapp.data.entity.BookNotes
import com.example.booktrackerapp.data.entity.BookWithNotes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).bookTrackerDao()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Sistema de búsqueda reactiva en tiempo real (Título o Autor)
    val filteredBooks: StateFlow<List<Books>> = _searchQuery
        .combine(dao.getAllBooks()) { query, booksList ->
            if (query.isBlank()) {
                booksList
            } else {
                booksList.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.author.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // Insertar un libro y volver automáticamente al finalizar con éxito
    fun insertBook(title: String, author: String, synopsis: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newBook = Books(title = title, author = author, synopsis = synopsis)
            dao.insertBook(newBook)
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    // Obtener la relación de un libro específico por ID para la pantalla de detalles
    fun getBookWithNotes(bookId: Int): Flow<BookWithNotes?> {
        return dao.getBooksWithNotes().map { list ->
            list.find { it.books.id == bookId }
        }
    }

    // Inserción rápida de notas
    fun insertNote(bookId: Int, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val newNote = BookNotes(
                bookId = bookId,
                content = content,
                dateTimestamp = System.currentTimeMillis()
            )
            dao.insertBookNote(newNote)
        }
    }

    fun deleteBook(books: Books, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteBook(books)
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun updateBook(books: Books) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateBook(books)
        }
    }

    fun updateNote(note: BookNotes, newContent: String) {
        if (newContent.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val updatedNote = note.copy(content = newContent, dateTimestamp = System.currentTimeMillis())
            dao.updateBookNote(updatedNote)
        }
    }

    fun deleteNote(note: BookNotes) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteBookNote(note)
        }
    }

}