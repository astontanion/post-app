package space.stanton.technicaltest.repository

import android.database.sqlite.SQLiteException
import space.stanton.technicaltest.database.PostDao
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostEntity
import space.stanton.technicaltest.network.ApiService
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService<PostEndPoint>,
    private val postDao: PostDao
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

    override suspend fun retrieveAllSavedPosts(): List<Post> {
        return postDao.retrieveAllPost().map { it.toPost() }
    }

    override suspend fun retrieveSavedPostWithId(postId: Int): Post {
        return postDao.retrievePostWithId(postId = postId)?.toPost()
            ?: throw SQLiteException()
    }

    override suspend fun savePost(post: Post): Post{
        val rowId = postDao.savePost(post.toPostEntity())

        if (rowId == 0L) {
            throw SQLiteException()
        }

        return post
    }

    override suspend fun deleteSavedPostWithId(postId: Int): Post {
        val postEntity = postDao.retrievePostWithId(postId) ?: throw SQLiteException()
        postDao.deletePostWithId(postEntity)
        return postEntity.toPost()
    }

}