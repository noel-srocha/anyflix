package dev.noelsrocha.anyflix.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.noelsrocha.anyflix.database.dao.MovieDao
import dev.noelsrocha.anyflix.database.entities.toMovie
import dev.noelsrocha.anyflix.model.Movie
import dev.noelsrocha.anyflix.ui.uistates.MyListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    private val dao: MovieDao
) : ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<MyListUiState>(
        MyListUiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {

            dao.myList()
                .onStart {
                    _uiState.update { MyListUiState.Loading }
                }
                .map { entities -> entities.map { it.toMovie() } }
                .collect { movies ->
                    _uiState.update {
                        if (movies.isEmpty()) {
                            MyListUiState.Empty
                        } else {
                            MyListUiState.Success(movies = movies)
                        }
                    }
                }
        }
    }

    suspend fun removeFromMyList(movie: Movie) {
        dao.removeFromMyList(movie.id)
    }

    fun loadMyList() {
        loadUiState()
    }

}