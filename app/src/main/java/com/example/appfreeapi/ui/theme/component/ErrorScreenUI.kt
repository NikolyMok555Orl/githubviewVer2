package com.example.appfreeapi.ui.theme.component

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appfreeapi.ui.theme.AppFreeApiTheme

@Composable
fun ErrorScreenUI(error: String, modifier: Modifier=Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenUIPreview() {
    AppFreeApiTheme() {
        ErrorScreenUI("Ошибка")
    }

}