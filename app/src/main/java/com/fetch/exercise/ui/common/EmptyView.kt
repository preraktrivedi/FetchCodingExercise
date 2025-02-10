package com.fetch.exercise.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fetch.exercise.ui.theme.FetchCodingExerciseTheme

@Composable
fun EmptyView(errorMsg: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = errorMsg,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 30.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyViewPreview() {
    FetchCodingExerciseTheme {
        EmptyView("No items available!")
    }
}
