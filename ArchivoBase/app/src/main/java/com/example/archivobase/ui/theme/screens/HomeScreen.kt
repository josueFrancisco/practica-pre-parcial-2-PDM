package com.example.archivobase.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.archivobase.ui.theme.components.TaskCard
import com.example.archivobase.viewmodel.TaskViewModel

@Composable
fun HomeScreen(viewModel: TaskViewModel){

    Column {

        Button(
            onClick = {
                viewModel.addTask("Nueva tarea")
            }
        ){
            Text("Agregar")
        }

        LazyColumn {

            items(viewModel.tasks){ task ->


                TaskCard(task)

            }

        }

    }
}

/*
✔ Mostrar pantalla completa.

✔ Leer datos del ViewModel.
 */