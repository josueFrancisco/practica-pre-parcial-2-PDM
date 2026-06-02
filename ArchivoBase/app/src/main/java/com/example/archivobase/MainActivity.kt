package com.example.archivobase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.archivobase.ui.theme.ArchivoBaseTheme
import com.example.archivobase.ui.theme.screens.HomeScreen
import com.example.archivobase.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val viewModel: TaskViewModel = TaskViewModel()

            HomeScreen(viewModel)

        }
    }
}
/*
Responsabilidad:

✔ Arrancar la aplicación.

✔ Conectar ViewModel con la pantalla.

NO meter lógica aquí.

 */