package space.stanton.technicaltest.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import space.stanton.technicaltest.R
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostDto
import java.io.BufferedInputStream
import java.io.IOException

class FakePostRepositoryImpl(context: Context): PostRepository {

    private var posts: List<Post> = listOf()

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
}