package com.example.booktrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booktrackerapp.ui.theme.BookTrackerAppTheme
import com.example.booktrackerapp.view.AddBookScreen
import com.example.booktrackerapp.view.BookDetailScreen
import com.example.booktrackerapp.view.BookListScreen
import com.example.booktrackerapp.view.HomeScreen
import com.example.booktrackerapp.viewModel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookTrackerAppTheme {
                val navController = rememberNavController()
                val sharedViewModel: BookViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    // Pantalla de Bienvenida (Splash Screen)
                    composable("home") {
                        HomeScreen(
                            onNavigateToMainList = {
                                navController.navigate("book_list") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        )
                    }

                    // Lista Principal y Buscador
                    composable("book_list") {
                        BookListScreen(
                            viewModel = sharedViewModel,
                            onNavigateToAddBook = { navController.navigate("add_book") },
                            onNavigateToDetail = { bookId -> navController.navigate("book_detail/$bookId") }
                        )
                    }

                    // Formulario para Agregar Libro
                    composable("add_book") {
                        AddBookScreen(
                            viewModel = sharedViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    // Detalle del Libro y Notas
                    composable(
                        route = "book_detail/{bookId}",
                        arguments = listOf(navArgument("bookId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
                        BookDetailScreen(
                            bookId = bookId,
                            viewModel = sharedViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}