package dev.noelsrocha.anyflix.di.modules

import android.content.Context
import androidx.room.Room
import dev.noelsrocha.anyflix.database.AnyflixDatabase
import dev.noelsrocha.anyflix.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context, ): AnyflixDatabase {
        return Room.databaseBuilder(context, AnyflixDatabase::class.java, "anyflix.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(db: AnyflixDatabase): MovieDao {
        return db.movieDao()
    }

}