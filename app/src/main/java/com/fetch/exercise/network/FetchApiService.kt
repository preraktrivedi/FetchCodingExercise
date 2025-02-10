package com.fetch.exercise.network

import com.fetch.exercise.data.model.ItemCollection
import retrofit2.Response
import retrofit2.http.GET

interface FetchHiringApiService {
    @GET("hiring.json")
    suspend fun getHiringList(): Response<ItemCollection>
}