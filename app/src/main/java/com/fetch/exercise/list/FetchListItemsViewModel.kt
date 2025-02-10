package com.fetch.exercise.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.exercise.data.model.ItemCollection
import com.fetch.exercise.repo.FetchItemsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel to host business logic for list items and expose the UiState
 */
class FetchListItemsViewModel: ViewModel() {

    // Created this UI model class as a transform to guard from API definition changes
    data class UiModel(
        val name: String,
        val listIdentifier: String,
        val navId: Long = -1,
    )

    data class UiState(
        val itemsList: List<UiModel> = listOf(),
        val errorMsg: String? = null,
    )

    // Using single instance of repo for now but can be passed via VM factory in future
    private val fetchItemsRepo: FetchItemsRepo by lazy {
        FetchItemsRepo.getInstance()
    }

    // Communicates UI state update to the corresponding screen
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchItems()
    }

    fun fetchItems() {
        fetchItemsRepo.getHiringList().onEach {
            Log.d(TAG, "Received ${it.size} items from network")
            val uiItemList = sanitizeResponse(it).toUiModel()
            Log.d(TAG, "Converted ${uiItemList.size} items to UI model")
            val errorMsg = when {
                uiItemList.isEmpty() -> "No list items available"
                else -> null
            }
            _uiState.value = UiState(
                itemsList = uiItemList,
                errorMsg = errorMsg,
            )
        }.launchIn(viewModelScope)
    }

    /**
     *     Requirements:
     *     - Display all the items grouped by "listId"
     *     - Sort the results first by "listId" then by "name" when displaying.
     *     - Filter out any items where "name" is blank or null
     */
    private fun sanitizeResponse(rawList: ItemCollection): ItemCollection {
        val listAfterEmptyFilter = rawList.filter { it.name?.isNotEmpty() == true }
        Log.d(TAG, "List count after empty list filtered: ${listAfterEmptyFilter.size}")
        return listAfterEmptyFilter.sortedWith(compareBy({it.listId}, {it.name}))
    }

    private fun ItemCollection.toUiModel() = map {
        UiModel(
            name = it.name ?: error("Name was null"),
            listIdentifier = it.listId.toString(),
            navId = it.id,
        )
    }

    companion object {
        private const val TAG = "FetchItemsViewModel"
    }
}


