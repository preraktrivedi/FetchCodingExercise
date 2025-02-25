package com.fetch.exercise.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fetch.exercise.R
import com.fetch.exercise.list.FetchListItemsViewModel
import com.fetch.exercise.ui.common.EmptyView
import com.fetch.exercise.ui.theme.FetchCodingExerciseTheme
import kotlin.random.Random

@Composable
fun FetchItemsListScreen(
    vm: FetchListItemsViewModel,
    onNavigateToItem: (Long) -> Unit,
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle().value
    when {
        uiState.errorMsg?.isNotEmpty() == true -> EmptyView(uiState.errorMsg)
        else -> ListItemsView(uiState.itemsList, onNavigateToItem, onDeleteRequested = { id ->
            vm.onDeleteItem(id)
        })
    }
}

@Composable
fun ListItemsView(
    itemsList: List<FetchListItemsViewModel.UiModel>,
    onNavigateToItem: (Long) -> Unit,
    onDeleteRequested: (id: Long) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 100.dp),
    ) {
        items(itemsList, key = { it.id }) { item ->
            SingleListItem(details = item,
                onClick = { itemId ->
                    onNavigateToItem(itemId)
                },
                onDeleteRequested = onDeleteRequested,
            )
        }
    }
}

@Composable
fun SingleListItem(
    details: FetchListItemsViewModel.UiModel,
    onClick: (navId: Long) -> Unit,
    onDeleteRequested: (id: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(details.navId) }
    ) {
        Text(
            text = details.name,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 14.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.text_list_id, details.listIdentifier),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        androidx.compose.material3.Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.cd_delete),
            modifier = Modifier.size(44.dp).clickable {
                onDeleteRequested(details.id)
            }
        )
        HorizontalDivider(color = Color.LightGray)
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemsPreview() {
    FetchCodingExerciseTheme {
        val sampleList = mutableListOf<FetchListItemsViewModel.UiModel>()
        for (i in 1..500) {
            sampleList.add(FetchListItemsViewModel.UiModel(
                navId = i.toLong(),
                listIdentifier = Random.nextInt(1,10).toString(),
                name = "Item $i"
            ))
        }
        ListItemsView(itemsList = sampleList, {}) {
            // Do nothing
        }
    }
}

