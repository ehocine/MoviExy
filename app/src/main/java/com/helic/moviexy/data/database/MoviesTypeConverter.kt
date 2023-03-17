package com.helic.moviexy.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.helic.moviexy.data.network.models.movie.Movie

class MoviesTypeConverter {
    var gson = Gson()


    @TypeConverter
    fun resultToString(foodRecipeResult: Movie): String {
        return gson.toJson(foodRecipeResult)
    }

    @TypeConverter
    fun stringToResult(data: String): Movie {
        val listType = object : TypeToken<Movie>() {}.type
        return gson.fromJson(data, listType)
    }
}