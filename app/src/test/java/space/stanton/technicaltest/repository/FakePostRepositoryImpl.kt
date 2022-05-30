package space.stanton.technicaltest.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostDto
import java.io.IOException
import java.nio.file.Files.newBufferedReader
import java.nio.file.Paths

class FakePostRepositoryImpl: PostRepository {

    private var posts: List<Post> = listOf()

    init {
        val reader = newBufferedReader(Paths.get("src/main/res/raw/posts.json"))
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