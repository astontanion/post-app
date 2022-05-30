package space.stanton.technicaltest.repository

import space.stanton.technicaltest.model.Comment

interface CommentRepository {
    suspend fun retrieveCommentForPostWithId(postId: Int): List<Comment>
}