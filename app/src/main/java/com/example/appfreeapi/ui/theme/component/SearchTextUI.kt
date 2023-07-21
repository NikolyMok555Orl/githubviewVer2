package com.example.appfreeapi.ui.theme.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appfreeapi.ui.theme.AppFreeApiTheme

/**Блок поиска*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextUI(
    query: String,
    onQueryChange: (newQuery: String) -> Unit,
    onSearchClick: (query: String) -> Unit,
    onCloseAndClear: () -> Unit,
    textLabel:String="Введите запрос",
    maxLines:Int = 1,
    modifier: Modifier = Modifier
) {

    Column() {
        Row(
            modifier = modifier
                .padding(8.dp, 4.dp)
        ) {
            val focusManager = LocalFocusManager.current
            OutlinedTextField(value = query,
                onValueChange = {
                    onQueryChange(it)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "")
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = onCloseAndClear) {
                            Icon(Icons.Default.Clear, contentDescription = "Очистить")
                        }
                    }
                },
                label = {
                    Text(
                        text = textLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClick(query)
                        //searchModel.insert(query)
                        focusManager.clearFocus()
                    }
                ),
                maxLines = maxLines,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchText() {
    AppFreeApiTheme() {
        SearchTextUI(
            query = "",
            onQueryChange = {},
            onSearchClick = {},
            onCloseAndClear = {},
        )
    }
}