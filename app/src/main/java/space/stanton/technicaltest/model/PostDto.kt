package space.stanton.technicaltest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostDto(
    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("userId")
    val userId: Int,

    @Expose
    @SerializedName("title")
    val title: String,

    @Expose
    @SerializedName("body")
    val content: String
) {
    fun toPost(): Post {
        return Post(
            id = id,
            userId = userId,
            title = title,
            content = content
        )
    }
}
