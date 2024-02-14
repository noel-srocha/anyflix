package dev.noelsrocha.anyflix.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.noelsrocha.anyflix.database.dao.MovieDao
import dev.noelsrocha.anyflix.database.entities.toMovie
import dev.noelsrocha.anyflix.model.Movie
import dev.noelsrocha.anyflix.navigation.movieIdArgument
import dev.noelsrocha.anyflix.ui.uistates.MovieDetailsUiState
import dev.noelsrocha.anyflix.ui.uistates.MovieDetailsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.noelsrocha.anyflix.repositories.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
) : ViewModel() {
    private var currentUiStateJob: Job? = null

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(
        MovieDetailsUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            repository.findMovieById(
                requireNotNull(
                    savedStateHandle[movieIdArgument]
                )
            ).onStart {
                _uiState.update { MovieDetailsUiState.Loading }
            }.flatMapLatest { movie ->
                repository.suggestedMovies(movie.id)
                    .map { suggestedMovies ->
                        Success(
                            movie = movie,
                            suggestedMovies = suggestedMovies,
                        )
                    }
            }.collectLatest { uiState ->
                _uiState.emit(uiState)
            }
        }
    }

    suspend fun addToMyList(movie: Movie) {
        repository.addToMyList(movie.id)
    }

    suspend fun removeFromMyList(movie: Movie) {
        repository.removeFromMyList(movie.id)
    }

    fun loadMovie() {
        loadUiState()
    }

}