package com.helic.moviexy.data.repository

import com.helic.moviexy.data.LocalDataSource
import com.helic.moviexy.data.RemoteDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}