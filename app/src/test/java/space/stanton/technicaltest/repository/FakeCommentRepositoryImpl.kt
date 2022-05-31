package space.stanton.technicaltest.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import space.stanton.technicaltest.model.Comment
import space.stanton.technicaltest.model.CommentDto
import space.stanton.technicaltest.model.Post
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class FakeCommentRepositoryImpl(): CommentRepository {

    private var comments: List<Comment> = listOf()

    init {
        val reader = Files.newBufferedReader(Paths.get("src/main/res/raw/post_comments.json"))
        val type = object: TypeToken<List<CommentDto>>() {}.type
        val commentsDto = Gson().fromJson<List<CommentDto>>(reader, type)
        comments = commentsDto.map { it.toComment() }
    }

    override suspend fun retrieveCommentForPostWithId(postId: Int): List<Comment> {
        val cmts = comments.filter { it.postId == postId }

        if (cmts.isNotEmpty()) { return cmts }

        when(postId == -1) {
            true -> throw IOException()
            false -> throw HttpException(Response.error<Post?>(500, "".toResponseBody()))
        }
    }
}