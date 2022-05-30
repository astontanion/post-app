package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import space.stanton.technicaltest.fragment.CommentListFragment
import space.stanton.technicaltest.usecase.RetrieveAllCommentForPostUseCase
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    private val retrieveAllCommentForPostUseCase: RetrieveAllCommentForPostUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(CommentListState())
    val state: SharedFlow<CommentListState> = _state

    init {
        val postId = savedStateHandle.get<Int>(CommentListFragment.ARG_POST_ID) ?: -1
        retrieveAllCommentForPostWithId(postId = postId)
    }

    fun retrieveAllCommentForPostWithId(postId: Int) {
        viewModelScope.launch {
            retrieveAllCommentForPostUseCase(postId = postId).collectLatest { result ->
                _state.value = _state.value.copy(
                    commentResource = result
                )
            }
        }
    }
}