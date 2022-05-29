package space.stanton.technicaltest.repository

import space.stanton.technicaltest.model.Post

interface PostRepository {
    suspend fun retrieveAllPosts(): List<Post>
    suspend fun retrievePostWithId(postId: Int): Post
}