package dev.noelsrocha.anyflix.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.noelsrocha.anyflix.database.dao.MovieDao
import dev.noelsrocha.anyflix.database.entities.MovieEntity

@Database(
    version = 1,
    entities = [MovieEntity::class],
)
abstract class AnyflixDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}