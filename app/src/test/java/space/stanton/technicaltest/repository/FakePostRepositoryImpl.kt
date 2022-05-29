package space.stanton.technicaltest.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostDto
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Files.newBufferedReader
import java.nio.file.Paths

class FakePostRepositoryImpl: PostRepository {

    override suspend fun retrieveAllPosts(): List<Post> {
        val reader = Files.newBufferedReader(Paths.get("res/raw/posts.json"))
        val type = object: TypeToken<List<PostDto>>() {}.type
        val postsDto = Gson().fromJson<List<PostDto>>(reader, type)
        return postsDto.map { it.toPost() }
    }

    override suspend fun retrievePostWithId(postId: Int): Post {
        val reader = Files.newBufferedReader(Paths.get("res/raw/posts.json"))
        val type = object: TypeToken<List<PostDto>>() {}.type
        val postsDto = Gson().fromJson<List<PostDto>>(reader, type)
        return postsDto.map { it.toPost() }.first { it.id == postId }
    }
}