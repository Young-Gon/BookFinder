package com.gondev.bookfinder.model.network.api

import com.gondev.bookfinder.model.network.dto.Result
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Google Book RESTFull API 목록
 * https://developers.google.com/books/docs/overview
 */
interface BookAPI {

    @GET("volumes")
    suspend fun requestBooks(
        @Query("q") query: String,
        @Query("maxResults") pageSize: Int = 20,
        @Query("startIndex") offset: Int = 0,
    ): Result
}