package space.stanton.technicaltest.repository

import space.stanton.technicaltest.model.Comment
import space.stanton.technicaltest.network.ApiService
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val apiService: ApiService<CommentEndPoint>
): CommentRepository {
    override suspend fun retrieveCommentForPostWithId(postId: Int): List<Comment> {
        val commentsDto = apiService.createService(serviceClass = CommentEndPoint::class.java)
            .retrieveAllCommentForPostWithId(postId = postId)
        return commentsDto.map { it.toComment() }
    }
}