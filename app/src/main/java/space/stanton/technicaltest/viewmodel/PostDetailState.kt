package space.stanton.technicaltest.viewmodel

import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource

data class PostDetailState(
    val postResource: DataResource<Post> = DataResource.Idle()
) {
    val isRefreshing = postResource is DataResource.Waiting
}
