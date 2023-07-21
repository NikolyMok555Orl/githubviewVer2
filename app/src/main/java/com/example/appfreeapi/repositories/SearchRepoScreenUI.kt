package com.example.appfreeapi.repositories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.appfreeapi.NavHost
import com.example.appfreeapi.data.model.RepositoryModel
import com.example.appfreeapi.ui.theme.component.ErrorScreenUI
import com.example.appfreeapi.ui.theme.component.LoadingScreenUI
import com.example.appfreeapi.ui.theme.component.SearchTextUI
import com.example.appfreeapi.utils.openBrowser


@Composable
fun SearchRepoScreenUI(
    navController: NavController, searchRepoVM: SearchRepoVM = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repoList = searchRepoVM.pagingDataFlow.collectAsLazyPagingItems()
    val stateUI = searchRepoVM.state.collectAsState()
    LaunchedEffect(key1 = true) {
        searchRepoVM.sharedFlowEffect.collect { effect ->
            when (effect) {
                is SearchRepoEffect.NavToUser -> {
                    navController.navigate("${NavHost.USER}/${effect.nameOwner}")
                }

                is SearchRepoEffect.OpenBrowser -> {
                    openBrowser(context, effect.url)
                }
            }
        }
    }


    SearchRepoScreenUI(stateUI.value, repoList, searchRepoVM::sendEvent)


}

@Composable
private fun SearchRepoScreenUI(
    stateUI: SearchStateUI,
    repoList: LazyPagingItems<RepositoryModel>,
    sendEvent: (event: SearchRepoEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SearchTextUI(
            query = stateUI.query,
            onQueryChange = { sendEvent(SearchRepoEvent.ChangeQuery(it)) },
            onSearchClick = {
                sendEvent(SearchRepoEvent.ClickSearch)
            },
            onCloseAndClear = {
                sendEvent(SearchRepoEvent.ClearQuery)
            })


        if (repoList.loadState.refresh is LoadState.Loading && !stateUI.isNotLoading) {
            LoadingScreenUI()
        } else if (repoList.loadState.refresh is LoadState.Error) {
            ErrorScreenUI(
                (repoList.loadState.refresh as LoadState.Error).error.message ?: "Произошла ошибка"
            )
        } else if (repoList.loadState.refresh !is LoadState.Loading) {
            LazyColumn(contentPadding = PaddingValues(4.dp), modifier = Modifier.weight(1f)) {
                items(repoList.itemCount) {
                    repoList[it]?.let { repository ->
                        RepoItemUI(
                            repository = repository,
                            onClick = { sendEvent(SearchRepoEvent.ClickToCart(repository)) },
                            onOpenUrl = { sendEvent(SearchRepoEvent.OpenRepoWithGitHub(repository.htmlUrl)) }
                        )
                    }

                }
            }
        }
    }
}
