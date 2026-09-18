package com.fikrisandi.parkeemovieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fikrisandi.parkeemovieapp.screen.detail.MovieDetailScreen
import com.fikrisandi.parkeemovieapp.screen.detail.MovieDetailViewModel
import com.fikrisandi.parkeemovieapp.screen.favorite.MovieFavoriteScreen
import com.fikrisandi.parkeemovieapp.screen.favorite.MovieFavoriteViewModel
import com.fikrisandi.parkeemovieapp.screen.home.HomeScreen
import com.fikrisandi.parkeemovieapp.screen.home.HomeViewModel
import com.fikrisandi.parkeemovieapp.ui.theme.ParkeeMovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkeeMovieAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        val viewModel: HomeViewModel = hiltViewModel()
                        HomeScreen(
                            viewModel = viewModel,
                            navController = navController,
                        )
                    }

                    composable("favorite") {
                        val viewModel: MovieFavoriteViewModel = hiltViewModel()
                        MovieFavoriteScreen(
                            viewModel = viewModel,
                            navController = navController,
                        )
                    }
                    composable(
                        route = "movie_detail/{movieId}",
                        arguments = listOf(navArgument("movieId") {
                            type = androidx.navigation.NavType.IntType
                        })
                    ) { backStackEntry ->
                        val movieId =
                            backStackEntry.arguments?.getInt("movieId") ?: return@composable
                        val viewModel: MovieDetailViewModel = hiltViewModel()

                        MovieDetailScreen(
                            movieId = movieId,
                            viewModel = viewModel,
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}
