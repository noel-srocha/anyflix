package dev.noelsrocha.anyflix.repositories

import android.util.Log
import dev.noelsrocha.anyflix.database.dao.MovieDao
import dev.noelsrocha.anyflix.database.entities.toMovie
import dev.noelsrocha.anyflix.extensions.toMovieEntity
import dev.noelsrocha.anyflix.model.Movie
import dev.noelsrocha.anyflix.services.MovieService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MovieRepository @Inject constructor(
    private val dao: MovieDao,
    private val service: MovieService
) {
    suspend fun findSections(): Flow<Map<String, List<Movie>>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.findAll()
                val entities = response.map { it.toMovieEntity() }

                dao.saveAll(*entities.toTypedArray())
            } catch(ex: ConnectException) {
                Log.e("MovieRepository", "findSections: Could not connect to server")
            } catch(ex: SocketTimeoutException) {
                Log.e("MovieRepository", "findSections: Request timed out")
            }
        }

        return dao.findAll().map { entities ->
            val movies = entities.map { it.toMovie() }

            if (entities.isEmpty()) {
                emptyMap()
            } else {
                createSections(movies)
            }
        }
    }

    suspend fun myList(): Flow<List<Movie>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.myList()
                val entities = response.map { it.toMovieEntity() }

                dao.saveAll(*entities.toTypedArray())
            } catch(ex: ConnectException) {
                Log.e("MovieRepository", "myList: Could not connect to server")
            } catch(ex: SocketTimeoutException) {
                Log.e("MovieRepository", "myList: Request timed out")
            }
        }


        return dao.myList().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    suspend fun findMovieById(id: String): Flow<Movie> {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = service.findMovieById(id)
                val entity = response.toMovieEntity()

                dao.save(entity)
            } catch(ex: ConnectException) {
                Log.e("MovieRepository", "findMovieById: Could not connect to server")
            } catch(ex: SocketTimeoutException) {
                Log.e("MovieRepository", "findMovieById: Request timed out")
            }
        }

        return dao.findMovieById(id).map { it.toMovie() }
    }

    fun suggestedMovies(id: String): Flow<List<Movie>> {
        return dao.suggestedMovies(id)
    }

    suspend fun addToMyList(id: String) {
        CoroutineScope(coroutineContext).launch {
            try {
                service.addToMyList(id)
                dao.addToMyList(id)
            } catch(ex: ConnectException) {
                Log.e("MovieRepository", "addToMyList: Could not connect to server")
            } catch(ex: SocketTimeoutException) {
                Log.e("MovieRepository", "addToMyList: Request timed out")
            }
        }
    }

    suspend fun removeFromMyList(id: String) {
        CoroutineScope(coroutineContext).launch {
            try {
                service.removeFromMyList(id)
                dao.removeFromMyList(id)
            } catch(ex: ConnectException) {
                Log.e("MovieRepository", "addToMyList: Could not connect to server")
            } catch(ex: SocketTimeoutException) {
                Log.e("MovieRepository", "addToMyList: Request timed out")
            }
        }
    }

    private fun createSections(movies: List<Movie>) = mapOf(
        "Em alta" to movies.shuffled().take(7),
        "Novidades" to movies.shuffled().take(7),
        "Continue assistindo" to movies.shuffled().take(7)
    )
}
