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

    private val deletedItemsSet = mutableSetOf<Long>()
    private var rawItemsList: ItemCollection = emptyList()

    fun getHiringList(useCache: Boolean = false): Flow<ItemCollection> = flow {
        val items: ItemCollection = if (useCache) {
            rawItemsList
        } else {
            networkDataSource.fetchItems().getOrElse { ex ->
                Log.e(TAG, "Error fetching items from network: ${ex.message}")
                emptyList()
            }
        }
        Log.d(TAG, "deletedItemsSet: $deletedItemsSet")
        rawItemsList = items.filterNot { deletedItemsSet.contains(it.id) }
        Log.d(TAG, "Item Count: ${rawItemsList.size}")
        emit(items)
    }.flowOn(dispatcher)

    fun getItemDetail(itemId: Long): Flow<ItemModel?> = flow {
        Log.d(TAG, "Finding $itemId in rawItemsList, count: ${rawItemsList.size}")
        emit(rawItemsList.find { it.id == itemId })
    }.flowOn(dispatcher)

    fun updateDeletion(id: Long) {
        Log.d("FETCH", "Delete item: $id")
        // Todo add an observer or make this reactive
        deletedItemsSet.add(id)
    }

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