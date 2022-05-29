package space.stanton.technicaltest.repository

import retrofit2.http.GET
import retrofit2.http.Path
import space.stanton.technicaltest.model.PostDto

interface PostEndPoint {

    @GET("posts")
    suspend fun retrieveAllPosts(): List<PostDto>

    @GET("posts/{id}")
    suspend fun retrievePostWithId(
        @Path("id") postId: Int
    ): PostDto
}