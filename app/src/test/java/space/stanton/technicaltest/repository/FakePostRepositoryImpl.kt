package space.stanton.technicaltest.repository

import android.database.sqlite.SQLiteException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostDto
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class FakePostRepositoryImpl(): PostRepository {

    private var posts: List<Post> = listOf()
    private val savedPosts: MutableList<Post> = mutableListOf()

    init {
        val reader = Files.newBufferedReader(Paths.get("src/main/res/raw/posts.json"))
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
        return savedPosts.first { it.id == postId }
    }

    override suspend fun deleteSavedPostWithId(postId: Int): Post {
        val post = savedPosts.first { it.id == postId }
        val wasRemoved = savedPosts.removeIf { it.id == postId }
        if (wasRemoved) { return post }

        throw SQLiteException()
    }

    override suspend fun savePost(post: Post): Post {
        savedPosts.add(post)
        return post
    }
}