package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import space.stanton.technicaltest.usecase.RetrievePostWithIdUseCase
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val retrievePostWithIdUseCase: RetrievePostWithIdUseCase,
    stateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state

    init {
        val postId = stateHandle.get<Int>("post_id") ?: -1
        retrievePostWithId(postId = postId)
    }

    fun retrievePostWithId(postId: Int) {
        viewModelScope.launch {
            retrievePostWithIdUseCase(postId = postId).collectLatest { result ->
                _state.value = _state.value.copy(
                    postResource = result
                )
            }
        }
    }
}