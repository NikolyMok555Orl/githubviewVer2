package com.example.appfreeapi.repositories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appfreeapi.R
import com.example.appfreeapi.data.model.RepositoryModel
import com.example.appfreeapi.ui.theme.AppFreeApiTheme
import kotlinx.coroutines.launch

@Composable
fun RepoItemUI(repository: RepositoryModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
       modifier= modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(onClick = { onClick() }),

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = modifier.fillMaxWidth()) {
                AsyncImage(
                    model = repository.avatarUrl,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .padding(5.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(percent = 10)
                        )
                        .size(50.dp)
                        .clip(RoundedCornerShape(percent = 10)),
                    placeholder = painterResource(R.drawable.ic_person)
                )
                Text(text = repository.fullName, style = MaterialTheme.typography.titleSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = repository.stargazersCount.toString())
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_baseline_star),
                        contentDescription = null, tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = repository.description, modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp
                )
                val composableScope = rememberCoroutineScope()
                val languages = remember { mutableStateOf("") }
                LaunchedEffect(key1 = true) {
                    if (repository.languages.isEmpty()) {
                        composableScope.launch {
                            languages.value = repository.getLanguages()
                        }
                    }
                }

                Text(text = "${languages.value}", fontStyle = FontStyle.Italic)
            }
            Text(
                text = repository.updatedAt, textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}



@Preview(showBackground = true)
@Composable
private fun RepoItemUIPreview() {
    AppFreeApiTheme() {
        RepoItemUI(RepositoryModel(id=0,
            fullName = "Плотва Андроид",
            nameOwner="Геральт",
            description = "Приложение для управление плотвой онлайн",
            languages= emptyList(),
            languagesUrl="",
            updatedAt = "2018-03-21T10:36:22Z",
            avatarUrl = "https://avatars.githubusercontent.com/u/37593827?v=4",
            stargazersCount = 4

        ), {})
    }
}