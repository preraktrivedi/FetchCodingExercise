package com.fetch.exercise.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fetch.exercise.R
import com.fetch.exercise.details.FetchItemDetailsViewModel
import com.fetch.exercise.ui.common.EmptyView
import com.fetch.exercise.ui.theme.FetchCodingExerciseTheme

@Composable
fun FetchItemsDetailsScreen(
    vm: FetchItemDetailsViewModel,
    onDoneClicked: () -> Unit,
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle().value
    when {
        uiState.errorMsg?.isNotEmpty() == true -> EmptyView(uiState.errorMsg)
        uiState.itemDetail != null -> ItemDetailView(uiState.itemDetail, onDoneClicked)
        else -> {
            // Do nothing, this may be the initial state - can show loading indicator based on VM state
        }
    }
}

@Composable
fun ItemDetailView(
    itemDetail: FetchItemDetailsViewModel.UiModel,
    onDoneClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().clickable(enabled = false) {},
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = itemDetail.name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 60.sp,
        )
        Text(
            text = stringResource(R.string.text_list_id, itemDetail.listIdentifier),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
        )
        Button(onClick = onDoneClicked,
            modifier = Modifier.fillMaxWidth(0.5f).padding(top = 40.dp)) {
            Text(
                text = stringResource(R.string.btn_done),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemDetailPreView() {
    FetchCodingExerciseTheme {
        ItemDetailView(itemDetail = FetchItemDetailsViewModel.UiModel(
            name = "Item 101",
            listIdentifier = "11")) {
            // Do nothing
        }
    }
}
