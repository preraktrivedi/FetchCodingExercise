package com.fetch.exercise.network

import com.fetch.exercise.data.model.ItemCollection

class FetchNetworkDataSource(
    private val apiService: FetchHiringApiService,
) {
    suspend fun fetchItems(): Result<ItemCollection> {
        return try {
            val response = apiService.getHiringList()
            if (response.isSuccessful && response.body() !=  null) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(IllegalStateException("Unsuccessful response: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(IllegalStateException("Invalid response: ${e.message}"))
        }
    }
}