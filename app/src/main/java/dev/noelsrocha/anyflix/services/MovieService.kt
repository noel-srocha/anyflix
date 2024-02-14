package dev.noelsrocha.anyflix.services

import dev.noelsrocha.anyflix.model.api.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface MovieService {

    @GET("movies")
    suspend fun findAll(): List<MovieResponse>

    @GET("movies/mylist")
    suspend fun myList(): List<MovieResponse>

    @GET("movies/{id}")
    suspend fun findMovieById(@Path("id") id: String): MovieResponse

    @PUT("movies/addToMyList/{id}")
    suspend fun addToMyList(@Path("id") id: String): Response<Void>

    @PUT("movies/removeFromMyList/{id}")
    suspend fun removeFromMyList(@Path("id") id: String): Response<Void>
}