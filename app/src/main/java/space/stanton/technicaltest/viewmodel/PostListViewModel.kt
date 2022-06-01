package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import space.stanton.technicaltest.fragment.PostListFragment
import space.stanton.technicaltest.usecase.RetrieveAllPostUseCase
import space.stanton.technicaltest.usecase.RetrieveAllSavedPostUseCase
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val retrieveAllPostUseCase: RetrieveAllPostUseCase,
    private val retrieveAllSavedPostUseCase: RetrieveAllSavedPostUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(PostListState())
    val state: StateFlow<PostListState> = _state

    init {
        val isOffline = savedStateHandle.get<Boolean>(PostListFragment.ARG_IS_FOR_OFFLINE) ?: false
       retrieveAllPosts(isOffline)
    }

    fun retrieveAllPosts(isOffline: Boolean) {
        viewModelScope.launch {
            when(isOffline) {
                true -> {
                    retrieveAllSavedPostUseCase().collectLatest { result ->
                        _state.value = _state.value.copy(
                            postResource = result
                        )
                    }
                }
                false -> {
                    retrieveAllPostUseCase().collectLatest { result ->
                        _state.value = _state.value.copy(
                            postResource = result
                        )
                    }
                }
            }
        }
    }
}