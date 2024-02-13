package dev.noelsrocha.anyflix.ui.uistates

import dev.noelsrocha.anyflix.model.Movie


sealed class MyListUiState {

    object Loading : MyListUiState()

    object Empty : MyListUiState()

    data class Success(
        val movies: List<Movie> = emptyList()
    ) : MyListUiState()

}
