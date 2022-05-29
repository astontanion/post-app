package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import space.stanton.technicaltest.usecase.RetrieveAllPostUseCase
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    private val retrieveAllPostUseCase: RetrieveAllPostUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(PostListState())
    val state: StateFlow<PostListState> = _state

    init {
       retrieveAllPosts()
    }

    fun retrieveAllPosts() {
        viewModelScope.launch {
            retrieveAllPostUseCase().collectLatest { result ->
                _state.value = _state.value.copy(
                    postResource = result
                )
            }
        }
    }
}