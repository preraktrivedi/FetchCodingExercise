package com.fetch.exercise.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FetchRetrofitImpl {

    companion object {
        private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
        private lateinit var instance: Retrofit

        fun getInstance(): Retrofit {
            if (::instance.isInitialized) {
                return instance
            }
            val httpClient = OkHttpClient.Builder()
                .build()
            return Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                    )
                )
                .build()
                .also {
                    instance = it
                }
        }
    }
}