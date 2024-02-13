package dev.noelsrocha.anyflix.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.noelsrocha.anyflix.model.Movie
import dev.noelsrocha.anyflix.ui.screens.MyListScreen
import dev.noelsrocha.anyflix.ui.viewmodels.MyListViewModel
import kotlinx.coroutines.launch

internal const val myListRoute = "myList"

fun NavGraphBuilder.myListScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToMovieDetails: (Movie) -> Unit
) {
    composable(myListRoute) {
        val viewModel = hiltViewModel<MyListViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()
        MyListScreen(
            uiState = uiState,
            onSeeOtherMovies = onNavigateToHome,
            onRemoveMovieFromMyList = {
                scope.launch {
                    viewModel.removeFromMyList(it)
                }
            },
            onMovieClick = onNavigateToMovieDetails,
            onRetryLoadMyList = {
                viewModel.loadMyList()
            }
        )
    }
}

fun NavController.navigateToMyList(
    navOptions: NavOptions? = null
) {
    navigate(myListRoute, navOptions)
}