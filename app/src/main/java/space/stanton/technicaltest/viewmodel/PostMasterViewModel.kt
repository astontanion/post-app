package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.usecase.RetrieveAllSavedPostUseCase
import javax.inject.Inject

@HiltViewModel
class PostMasterViewModel @Inject constructor(
    private val retrieveAllSavedPostUseCase: RetrieveAllSavedPostUseCase
): ViewModel() {

    private val _lastSelectedPost = MutableLiveData<Post?>(null)
    val lastSelectedPost: LiveData<Post?> = _lastSelectedPost

    private val _badgeCount = MutableStateFlow(0)
    val badgeCount: StateFlow<Int> = _badgeCount

    init {
        getBadgeCount()
    }

    fun onLastSelectedPostChange(post: Post?) {
        _lastSelectedPost.value = post
    }

    private fun getBadgeCount() {
        viewModelScope.launch {
            retrieveAllSavedPostUseCase().collectLatest { result ->
                if (result.data != null)  {
                    _badgeCount.value = result.data.size
                }
            }
        }
    }

}