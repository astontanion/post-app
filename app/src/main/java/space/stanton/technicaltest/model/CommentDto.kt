package space.stanton.technicaltest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommentDto(
    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("postId")
    val postId: Int,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("email")
    val email: String,

    @Expose
    @SerializedName("body")
    val content: String
) {
    fun toComment(): Comment {
        return Comment(
            id = id,
            postId = postId,
            name = name,
            email = email,
            content = content
        )
    }
}