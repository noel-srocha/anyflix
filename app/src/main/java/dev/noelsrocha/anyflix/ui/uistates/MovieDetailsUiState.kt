package dev.noelsrocha.anyflix.ui.uistates

import dev.noelsrocha.anyflix.model.Movie

sealed class MovieDetailsUiState {

    object Loading : MovieDetailsUiState()

    data class Success(
        val movie: Movie,
        val suggestedMovies: List<Movie> = emptyList()
    ) : MovieDetailsUiState()
}
