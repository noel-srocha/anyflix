package dev.noelsrocha.anyflix.extensions

import dev.noelsrocha.anyflix.database.entities.MovieEntity
import dev.noelsrocha.anyflix.model.Movie
import dev.noelsrocha.anyflix.model.api.MovieResponse

fun MovieResponse.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        year = year,
        plot = plot,
        image = image,
        inMyList = inMyList
    )
}

fun MovieResponse.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        year = year,
        plot = plot,
        image = image,
        inMyList = inMyList
    )
}