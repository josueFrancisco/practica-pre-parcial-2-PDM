package com.example.archivobase.ui.theme.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.archivobase.model.Task

@Composable
fun TaskCard(task: Task){

    Card {

        Text(task.title)

    }
}

/*
✔ Botones.

✔ Cards.

✔ Inputs.

✔ Elementos reutilizables.
 */