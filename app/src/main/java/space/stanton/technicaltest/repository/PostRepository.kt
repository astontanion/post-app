package space.stanton.technicaltest.repository

import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostEntity

interface PostRepository {
    suspend fun retrieveAllPosts(): List<Post>
    suspend fun retrievePostWithId(postId: Int): Post
    suspend fun retrieveAllSavedPosts(): List<Post>
    suspend fun retrieveSavedPostWithId(postId: Int): Post
    suspend fun deleteSavedPostWithId(postId: Int): Post
    suspend fun savePost(post: Post): Post
}