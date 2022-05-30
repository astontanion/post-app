package space.stanton.technicaltest.viewmodel

import space.stanton.technicaltest.model.Comment
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource

data class CommentListState(
    val commentResource: DataResource<List<Comment>> = DataResource.Idle()
) {
    val isRefreshing = commentResource is DataResource.Waiting
}
