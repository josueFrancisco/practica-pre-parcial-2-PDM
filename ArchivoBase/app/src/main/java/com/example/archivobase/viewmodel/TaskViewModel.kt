package com.example.archivobase.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.archivobase.model.Task

class TaskViewModel : ViewModel() {

    private val _tasks = mutableStateListOf<Task>()

    val tasks: List<Task>
        get() = _tasks

    fun addTask(title: String){

        _tasks.add(
            Task(title,false)
        )
    }

}

/*
Responsabilidades del ViewModel:

✔ Guardar estados.

✔ Manejar lógica.

✔ Actualizar datos.

NO dibujar UI.
 */