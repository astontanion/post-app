package space.stanton.technicaltest.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import space.stanton.technicaltest.R
import space.stanton.technicaltest.model.Comment
import space.stanton.technicaltest.model.CommentDto
import space.stanton.technicaltest.model.Post
import java.io.BufferedInputStream
import java.io.IOException

class FakeCommentRepositoryImpl(context: Context): CommentRepository {

    private var comments: List<Comment> = listOf()

    init {
        val inputStream = context.resources.openRawResource(R.raw.post_comments)
        val reader = BufferedInputStream(inputStream).bufferedReader()
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