package com.example.appfreeapi.repositories.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appfreeapi.R
import com.example.appfreeapi.repositories.data.model.RepositoryModel
import com.example.appfreeapi.ui.theme.AppFreeApiTheme

@Composable
fun RepoItemUI(
    repository: RepositoryModel,
    onClick: () -> Unit,
    onOpenUrl: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(onClick = { onClick() }),

        ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)) {
            Row(modifier = modifier.fillMaxWidth()) {
                AsyncImage(
                    model = repository.avatarUrl,
                    contentDescription = stringResource(R.string.avatar),
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
                Text(text = repository.fullName, style = MaterialTheme.typography.titleSmall,
                    modifier= Modifier.weight(1f))
                Row(
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
                Text(text = repository.languages.joinToString(), fontStyle = FontStyle.Italic)

                Text(text = stringResource(id = R.string.count_watcher, repository.watchers))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                    onOpenUrl()
                }) {
                    Text(
                        text = repository.htmlUrl,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.open_link),
                        contentDescription = stringResource(R.string.link_url),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(18.dp),
                    )
                }

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
        RepoItemUI(
            RepositoryModel(
            id = 0,
            fullName = "https://www.youtube.com/watch432523587325983275832532532",
            nameOwner = "Геральт",
            description = "Приложение для управление плотвой онлайн",
            languages = emptyList(),
            languagesUrl = "",
            updatedAt = "2018-03-21T10:36:22Z",
            avatarUrl = "https://avatars.githubusercontent.com/u/37593827?v=4 532532532532523532",
            stargazersCount = 4,
            languagesMain = "Kotlin",
            watchers = 0,
            htmlUrl = "https://github.com/thackl/gggenomes"

        ), {}, {})
    }
}