package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import space.stanton.technicaltest.model.Post
import javax.inject.Inject

@HiltViewModel
class PostMasterViewModel @Inject constructor(): ViewModel() {

    private val _lastSelectedPost = MutableLiveData<Post?>(null)
    val lastSelectedPost: LiveData<Post?> = _lastSelectedPost

    fun onLastSelectedPostChange(post: Post?) {
        _lastSelectedPost.value = post
    }
}