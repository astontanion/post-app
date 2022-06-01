package space.stanton.technicaltest.viewmodel

import kotlinx.coroutines.flow.Flow
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource

data class PostListState(
    val postResource: DataResource<List<Post>> = DataResource.Idle(),
) {
    val isRefreshing = postResource is DataResource.Waiting
}
