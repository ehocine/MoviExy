package com.helic.moviexy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helic.moviexy.data.network.models.movie.Movie
import com.helic.moviexy.utils.Constants.FAVORITE_MOVIES_TABLE

@Entity(tableName = FAVORITE_MOVIES_TABLE)
class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var movie: Movie = Movie()
)