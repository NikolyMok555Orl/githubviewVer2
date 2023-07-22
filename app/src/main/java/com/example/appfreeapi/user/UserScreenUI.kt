package com.example.appfreeapi.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appfreeapi.R
import com.example.appfreeapi.user.data.model.UserModel
import com.example.appfreeapi.ui.theme.AppFreeApiTheme
import com.example.appfreeapi.ui.theme.component.LoadingScreenUI
import com.example.appfreeapi.utils.openBrowser


@Composable
fun UserScreen(
    navController: NavController, userUrl: String,
    vm: UserVM = viewModel(
        factory = UserVM.provideFactory(
            userOwner = userUrl,
            owner = LocalSavedStateRegistryOwner.current
        )
    )
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        vm.sharedFlowEffect.collect { effect ->
            when (effect) {
                is UserEffect.OpenBrowser -> {
                    openBrowser(context,effect.url )
                }
            }
        }
    }

    val state = vm.state.collectAsState()
    when (state.value) {
        is UserState.Error -> {}
        UserState.Loading -> LoadingScreenUI()
        is UserState.Success -> {
            UserScreenUI((state.value as UserState.Success).user, vm::sendEvent)

        }
    }
}


@Composable
fun UserScreenUI(
    user: UserModel,
    sendEvent: (event: UserAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Surface(color = MaterialTheme.colorScheme.secondary) {
                Column(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = stringResource(id = R.string.avatar),
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(5.dp),
                        placeholder = painterResource(R.drawable.ic_person)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .padding(bottom = 10.dp)
                            .padding(bottom = 0.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${user.followers}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(text = stringResource(R.string.followers), style = MaterialTheme.typography.labelMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${user.following}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(text = stringResource(R.string.following), style = MaterialTheme.typography.labelMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${user.publicRepos}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(text = stringResource(R.string.count_public_repo), style = MaterialTheme.typography.labelMedium)
                        }


                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(8.dp)
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.displaySmall.copy(),
                )
                Text(text = user.type, style = MaterialTheme.typography.titleSmall.copy(fontFamily = FontFamily.Monospace))
            }

            Text(
                text = stringResource(R.string.about_me),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier=Modifier.padding(vertical = 10.dp)
            )
            Text(text = user.bio, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = stringResource(id = R.string.links_url),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier=Modifier.padding(top = 10.dp)
            )
            if (user.blog.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = {
                        sendEvent(UserAction.OpenBrowser(user.blog))
                    }, contentPadding = PaddingValues(0.dp)) {
                        Text(
                            text = user.blog,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.open_link),
                            contentDescription = stringResource(id = R.string.link_url),
                            modifier = Modifier.padding(start = 4.dp),
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = {
                    sendEvent(UserAction.OpenBrowser(user.htmlUrl),
                       )
                },  contentPadding = PaddingValues(0.dp)) {
                    Text(
                        text = user.htmlUrl,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.open_link),
                        contentDescription = stringResource(id = R.string.link_url),
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun UserScreenUIPreview() {
    AppFreeApiTheme() {
        UserScreenUI(
            UserModel(
                id = 22032646,
                name = "tidyverse",
                bio = "The tidyverse is a collection of R packages that share common principles and are designed to work together seamlessly",
                avatarUrl = "https://i.playground.ru/i/pix/1484606/image.jpg",
                followers = 794,
                following = 0,
                blog = "http://tidyverse.org",
                htmlUrl = "https://github.com/tidyverse",
                type = "Organization",
                publicRepos = 40,
                createdAt = "2016-09-06T16:05:40Z",
                updatedAt = "2023-07-13T11:28:32Z",
            ), {}
        )
    }
}

