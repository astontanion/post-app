package space.stanton.technicaltest.repository

import retrofit2.http.GET
import retrofit2.http.Path
import space.stanton.technicaltest.model.CommentDto

interface CommentEndPoint {
    @GET("posts/{post_id}/comments")
    suspend fun retrieveAllCommentForPostWithId(
        @Path("post_id") postId: Int
    ): List<CommentDto>
}