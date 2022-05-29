package space.stanton.technicaltest.repository

import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.ApiService
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService<PostEndPoint>
): PostRepository {

    override
    suspend fun retrieveAllPosts(): List<Post> {
        val postsDto = apiService.createService(PostEndPoint::class.java)
            .retrieveAllPosts()
        return postsDto.map { it.toPost() }
    }

    override
    suspend fun retrievePostWithId(postId: Int): Post {
        val postDto = apiService.createService(PostEndPoint::class.java)
            .retrievePostWithId(postId = postId)

        return postDto.toPost()
    }

}