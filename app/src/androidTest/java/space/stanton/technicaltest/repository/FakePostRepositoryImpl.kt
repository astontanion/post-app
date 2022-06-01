package space.stanton.technicaltest.repository

import android.content.Context
import android.database.sqlite.SQLiteException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import space.stanton.technicaltest.R
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostDto
import space.stanton.technicaltest.model.PostEntity
import java.io.BufferedInputStream
import java.io.IOException

class FakePostRepositoryImpl(context: Context): PostRepository {

    private var posts: List<Post> = listOf()
    private val savedPosts: MutableList<Post> = mutableListOf()

    init {
        val inputStream = context.resources.openRawResource(R.raw.posts)
        val reader = BufferedInputStream(inputStream).bufferedReader()
        val type = object: TypeToken<List<PostDto>>() {}.type
        val postsDto = Gson().fromJson<List<PostDto>>(reader, type)
        posts = postsDto.map { it.toPost() }
    }

    override suspend fun retrieveAllPosts(): List<Post> {
        return posts
    }

    override suspend fun retrievePostWithId(postId: Int): Post {
        val post = posts.firstOrNull { it.id == postId }

        if (post != null) { return post }

        if (postId == -1) {
            throw IOException()
        }

        throw HttpException(Response.error<Post?>(500, "".toResponseBody()))
    }

    override suspend fun retrieveAllSavedPosts(): Flow<List<Post>> {
        return flow<List<Post>> { emit(savedPosts) }
    }

    override suspend fun retrieveSavedPostWithId(postId: Int): Post {
        return savedPosts.firstOrNull { it.id == postId } ?: throw SQLiteException()
    }

    override suspend fun deleteSavedPostWithId(postId: Int): Post {
        val post = savedPosts.firstOrNull { it.id == postId } ?: throw SQLiteException()
        val wasRemoved = savedPosts.remove(post)

        if (wasRemoved) { return post }

        throw SQLiteException()
    }

    override suspend fun savePost(post: Post): Post {
        savedPosts.add(post)
        return post
    }
}