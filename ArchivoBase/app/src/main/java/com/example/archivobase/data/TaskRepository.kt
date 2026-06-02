package com.example.archivobase.data

import com.example.archivobase.model.Task

class TaskRepository {

    fun getTasks(): List<Task>{

        return listOf(
            Task("Estudiar",false),
            Task("Comer",true)
        )
    }

}

/*
Manejo de datos.
 */