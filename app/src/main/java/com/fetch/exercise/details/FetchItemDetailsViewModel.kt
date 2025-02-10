package com.fetch.exercise.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fetch.exercise.repo.FetchItemsRepo
import com.fetch.exercise.util.Constants.KEY_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FetchItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    data class UiModel(
        val name: String,
        val listIdentifier: String,
    )

    data class UiState(
        val itemDetail: UiModel? = null,
        val errorMsg: String? = null,
    )

    private val navId: Long = checkNotNull(savedStateHandle[KEY_ID])

    // Using single instance of repo for now but can be passed via VM factory in future
    private val fetchItemsRepo: FetchItemsRepo by lazy {
        FetchItemsRepo.getInstance()
    }

    // Communicates UI state update to the corresponding screen
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchItemDetail()
    }

    private fun fetchItemDetail() {
        Log.d(TAG, "Fetching item detail for id  $navId")
        fetchItemsRepo.getItemDetail(navId).onEach { itemModel ->
            Log.d(TAG, "Found item: $itemModel")
            val uiModel = if (itemModel != null) {
                UiModel(
                    name = itemModel.name ?: error("Name was null!"),
                    listIdentifier = itemModel.listId.toString()
                )
            } else null
            val errorMsg = if (itemModel == null) {
                "Could not find item detail for id: $navId"
            } else null
            _uiState.value = UiState(
                itemDetail = uiModel,
                errorMsg = errorMsg,
            )
        }.launchIn(viewModelScope)
    }

    companion object {
        private const val TAG = "FetchItemDetailsViewModel"
    }
}