package com.example.booktrackerapp.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.booktrackerapp.data.entity.Books
import com.example.booktrackerapp.data.entity.BookNotes
import com.example.booktrackerapp.viewModel.BookViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: Int,
    viewModel: BookViewModel,
    onNavigateBack: () -> Unit
) {
    val bookWithNotesState by viewModel.getBookWithNotes(bookId).collectAsState(initial = null)

    // Estados para control de edición de Libro
    var isEditingBook by remember { mutableStateOf(false) }
    var editTitle by remember { mutableStateOf("") }
    var editAuthor by remember { mutableStateOf("") }
    var editSynopsis by remember { mutableStateOf("") }

    // Estados para gestión de Notas
    var newNoteContent by remember { mutableStateOf("") }
    var showDeleteBookDialog by remember { mutableStateOf(false) }
    var noteSelectedForAction by remember { mutableStateOf<BookNotes?>(null) }
    var showEditNoteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditingBook) "Editar Libro" else "Detalle del Libro") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    bookWithNotesState?.let { bookWithNotes ->
                        if (isEditingBook) {
                            // Botón de confirmar cambios del libro
                            IconButton(onClick = {
                                if (editTitle.isNotBlank() && editAuthor.isNotBlank()) {
                                    val updatedBook = bookWithNotes.books.copy(
                                        title = editTitle,
                                        author = editAuthor,
                                        synopsis = editSynopsis
                                    )
                                    viewModel.updateBook(updatedBook)
                                    isEditingBook = false
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.primary)
                            }
                        } else {
                            // Botón para entrar en modo edición de libro
                            IconButton(onClick = {
                                editTitle = bookWithNotes.books.title
                                editAuthor = bookWithNotes.books.author
                                editSynopsis = bookWithNotes.books.synopsis
                                isEditingBook = true
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Libro")
                            }
                            // Botón para borrar libro
                            IconButton(onClick = { showDeleteBookDialog = true }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Libro", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        // Diálogo de Confirmación para Eliminar Libro
        if (showDeleteBookDialog) {
            bookWithNotesState?.let {
                AlertDialog(
                    onDismissRequest = { showDeleteBookDialog = false },
                    title = { Text("¿Eliminar este libro?") },
                    text = { Text("Se borrará definitivamente \"${it.books.title}\" junto con todas sus notas.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteBookDialog = false
                            viewModel.deleteBook(it.books) { onNavigateBack() }
                        }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = { TextButton(onClick = { showDeleteBookDialog = false }) { Text("Cancelar") } }
                )
            }
        }

        // Diálogo Opciones de Nota (Editar / Eliminar)
        noteSelectedForAction?.let { note ->
            AlertDialog(
                onDismissRequest = { noteSelectedForAction = null },
                title = { Text("Opciones de nota") },
                text = { Text("¿Qué deseas hacer con este comentario?") },
                confirmButton = {
                    TextButton(onClick = {
                        showEditNoteDialog = true
                    }) { Text("Editar") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.deleteNote(note)
                        noteSelectedForAction = null
                    }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                        Text("Eliminar")
                    }
                }
            )
        }

        // Diálogo específico para editar el cuerpo de la nota
        if (showEditNoteDialog && noteSelectedForAction != null) {
            var noteContentChanges by remember { mutableStateOf(noteSelectedForAction!!.content) }
            AlertDialog(
                onDismissRequest = { showEditNoteDialog = false; noteSelectedForAction = null },
                title = { Text("Editar Nota") },
                text = {
                    OutlinedTextField(
                        value = noteContentChanges,
                        onValueChange = { noteContentChanges = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.updateNote(noteSelectedForAction!!, noteContentChanges)
                        showEditNoteDialog = false
                        noteSelectedForAction = null
                    }) { Text("Actualizar") }
                },
                dismissButton = {
                    TextButton(onClick = { showEditNoteDialog = false; noteSelectedForAction = null }) { Text("Cancelar") }
                }
            )
        }

        // Cuerpo Principal
        bookWithNotesState?.let { bookWithNotes ->
            val book = bookWithNotes.books
            val sortedNotes = bookWithNotes.notesList.sortedByDescending { it.dateTimestamp }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (isEditingBook) {
                    // Formulario de edición interactivo
                    OutlinedTextField(value = editTitle, onValueChange = { editTitle = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = editAuthor, onValueChange = { editAuthor = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = editSynopsis, onValueChange = { editSynopsis = it }, label = { Text("Sinopsis") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                } else {
                    // Vista Normal de lectura
                    Text(text = book.title, style = MaterialTheme.typography.headlineMedium)
                    Text(text = "Por: ${book.author}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = book.synopsis, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 8.dp))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                Text(text = "Notas y Comentarios (Mantén presionado para opciones)", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.outline)

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (sortedNotes.isEmpty()) {
                        item {
                            Text(text = "No hay notas aún para este libro.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        items(sortedNotes) { note ->
                            NoteItemRow(note = note, onLongClick = { noteSelectedForAction = note })
                        }
                    }
                }

                // Inserción rápida inferior
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newNoteContent,
                        onValueChange = { newNoteContent = it },
                        label = { Text("Escribir una nota...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newNoteContent.isNotBlank()) {
                                viewModel.insertNote(book.id, newNoteContent)
                                newNoteContent = ""
                            }
                        }
                    ) { Text("Añadir") }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemRow(note: BookNotes, onLongClick: () -> Unit) {
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val dateString = sdf.format(Date(note.dateTimestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = onLongClick, // Abre el diálogo de edición/borrado al mantener presionado
                onClick = {}
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dateString, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}