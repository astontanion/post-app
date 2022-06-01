package space.stanton.technicaltest.repository

import kotlinx.coroutines.flow.Flow
import space.stanton.technicaltest.model.Post

interface PostRepository {
    suspend fun retrieveAllPosts(): List<Post>
    suspend fun retrievePostWithId(postId: Int): Post
    suspend fun retrieveAllSavedPosts(): Flow<List<Post>>
    suspend fun retrieveSavedPostWithId(postId: Int): Post
    suspend fun deleteSavedPostWithId(postId: Int): Post
    suspend fun savePost(post: Post): Post
}