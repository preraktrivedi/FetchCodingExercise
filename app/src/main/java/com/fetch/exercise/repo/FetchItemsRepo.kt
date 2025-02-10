package com.fetch.exercise.repo

import android.util.Log
import com.fetch.exercise.data.model.ItemCollection
import com.fetch.exercise.data.model.ItemModel
import com.fetch.exercise.network.FetchHiringApiService
import com.fetch.exercise.network.FetchNetworkDataSource
import com.fetch.exercise.network.FetchRetrofitImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repo can either get data from network or a local db/memory (in future)
 * Currently using a single repo since data is shared
 */
class FetchItemsRepo private constructor(
    private val networkDataSource: FetchNetworkDataSource,
    private val dispatcher: CoroutineDispatcher,
) {

    private var rawItemsList: ItemCollection = emptyList()

    fun getHiringList(): Flow<ItemCollection> = flow {
        val items: ItemCollection = networkDataSource.fetchItems().getOrElse { ex ->
            Log.e(TAG, "Error fetching items from network: ${ex.message}")
            emptyList()
        }
        Log.d(TAG, "Item Count: ${items.size}")
        rawItemsList = items
        emit(items)
    }.flowOn(dispatcher)

    fun getItemDetail(itemId: Long): Flow<ItemModel?> = flow {
        Log.d(TAG, "Finding $itemId in rawItemsList, count: ${rawItemsList.size}")
        emit(rawItemsList.find { it.id == itemId })
    }.flowOn(dispatcher)

    companion object {
        private const val TAG = "FetchItemsRepo"
        private lateinit var instance: FetchItemsRepo

        fun getInstance(): FetchItemsRepo {
            if (::instance.isInitialized) {
                return instance
            }
            val networkDataSource = FetchNetworkDataSource(
                apiService = FetchRetrofitImpl.getInstance().create(FetchHiringApiService::class.java),
            )
            return FetchItemsRepo(networkDataSource, Dispatchers.IO).also {
                instance = it
            }
        }
    }
}