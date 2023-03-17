package com.helic.moviexy.data

import com.helic.moviexy.data.database.FavoriteMovieEntity
import com.helic.moviexy.data.database.MoviesDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val moviesDAO: MoviesDAO
) {

    suspend fun insertFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        return moviesDAO.insertFavoriteMovie(favoriteMovieEntity = favoriteMovieEntity)
    }

    fun readFavoriteMovies(): Flow<List<FavoriteMovieEntity>> {
        return moviesDAO.readFavoriteMovies()
    }

    suspend fun deleteFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        return moviesDAO.deleteFavoriteMovie(favoriteMovieEntity = favoriteMovieEntity)
    }

    suspend fun deleteAllFavoriteMovies() {
        return moviesDAO.deleteAllFavoriteMovies()
    }
}