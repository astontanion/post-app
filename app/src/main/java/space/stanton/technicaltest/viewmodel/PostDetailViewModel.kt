package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import space.stanton.technicaltest.fragment.PostDetailFragment
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.GenericFailureReason
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.usecase.DeleteSavedPostUseCase
import space.stanton.technicaltest.usecase.RetrievePostWithIdUseCase
import space.stanton.technicaltest.usecase.RetrieveSavedPostWithIdUseCase
import space.stanton.technicaltest.usecase.SavePostUseCase
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val retrievePostWithIdUseCase: RetrievePostWithIdUseCase,
    private val retrieveSavedPostWithIdUseCase: RetrieveSavedPostWithIdUseCase,
    private val savePostUseCase: SavePostUseCase,
    private val deleteSavedPostUseCase: DeleteSavedPostUseCase,
    stateHandle: SavedStateHandle
): ViewModel() {

    lateinit var seeCommentClick: () -> Unit
    lateinit var savePostOffline: () -> Unit

    private val _state = MutableStateFlow(PostDetailState())
    val state: StateFlow<PostDetailState> = _state

    init {
        val postId = stateHandle.get<Int>(PostDetailFragment.ARG_POST_ID) ?: -1
        retrievePostWithId(postId = postId)
        retrieveSavedPostWithId(postId = postId)
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

    private fun retrieveSavedPostWithId(postId: Int) {
        viewModelScope.launch {
            retrieveSavedPostWithIdUseCase(postId = postId).collectLatest { result ->
                _state.value = _state.value.copy(
                    savedPostResource = result
                )
            }
        }
    }

    fun savePost() {
        _state.value.postResource.data?.let { post ->

            viewModelScope.launch {
                when(_state.value.savedPostResource.data == null) {
                    true -> {
                        savePostUseCase(post = post).collectLatest { result ->
                            _state.value = _state.value.copy(
                                savedPostResource = result
                            )
                        }
                    }
                    false -> {
                        deleteSavedPostUseCase(postId = post.id).collectLatest { result ->
                            _state.value = _state.value.copy(
                                savedPostResource = result
                            )
                        }
                    }
                }

            }
        } ?: run {
            _state.value = _state.value.copy(
                savedPostResource = DataResource.Failure(
                    operation = Operation.GET,
                    reason = GenericFailureReason.UNKNOWN
                )
            )
        }
    }
}