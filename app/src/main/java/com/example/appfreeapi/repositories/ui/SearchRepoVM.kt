package com.example.appfreeapi.repositories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.appfreeapi.repositories.data.RepositoryRepo
import com.example.appfreeapi.repositories.data.model.RepositoryModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepoVM(private val repositoryRepo: RepositoryRepo = RepositoryRepo()) : ViewModel() {


    private val _state: MutableStateFlow<SearchStateUI> = MutableStateFlow(SearchStateUI("", true))
    val state: StateFlow<SearchStateUI> = _state

    val pagingDataFlow: Flow<PagingData<RepositoryModel>>

    private val _sharedFlowEffect = MutableSharedFlow<SearchRepoEffect>()
    val sharedFlowEffect = _sharedFlowEffect.asSharedFlow()
    private val actionSearch = MutableSharedFlow<String>()

    init {
        pagingDataFlow = actionSearch
            .flatMapLatest {
                if(it.isNotEmpty()) {
                    _state.emit(_state.value.copy(isNotLoading = false))
                }
                searchRepo(query = it)
            }
            .cachedIn(viewModelScope)
    }

    fun sendEvent(event: SearchRepoEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchRepoEvent.ChangeQuery -> {
                    _state.emit(_state.value.copy(query = event.query))
                }
                SearchRepoEvent.ClickSearch -> {
                    actionSearch.emit(_state.value.query)
                }

                is SearchRepoEvent.ClickToCart -> {
                    _sharedFlowEffect.emit(SearchRepoEffect.NavToUser(event.repoModel.nameOwner))

                }

                SearchRepoEvent.ClearQuery -> {
                    _state.emit(_state.value.copy(query = "", isNotLoading = true))

                }

                is SearchRepoEvent.OpenRepoWithGitHub -> {

                    _sharedFlowEffect.emit(SearchRepoEffect.OpenBrowser(event.url))

                }
            }
        }
    }


    private fun searchRepo(query: String): Flow<PagingData<RepositoryModel>> =
        repositoryRepo.getSearchResult(query)

}


sealed class SearchRepoEvent() {


    data class ChangeQuery(val query: String) : SearchRepoEvent()

    object ClickSearch : SearchRepoEvent()

    object ClearQuery: SearchRepoEvent()

    data class ClickToCart(val repoModel: RepositoryModel) : SearchRepoEvent()

    data class OpenRepoWithGitHub(val url: String) : SearchRepoEvent()

}

sealed class SearchRepoEffect() {
    data class NavToUser(val nameOwner: String) : SearchRepoEffect()
    data class OpenBrowser(val url:String): SearchRepoEffect()

}

data class SearchStateUI(val query: String, val isNotLoading:Boolean=true)





