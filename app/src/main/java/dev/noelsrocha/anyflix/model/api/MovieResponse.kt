package dev.noelsrocha.anyflix.model.api

data class MovieResponse(
    val id: String,
    val title: String,
    val image: String,
    val year: Int,
    val plot: String,
    val inMyList: Boolean
)
