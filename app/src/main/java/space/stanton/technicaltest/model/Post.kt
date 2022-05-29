package space.stanton.technicaltest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val content: String
) {
    fun toPostDto(): PostDto {
        return PostDto(
            id = id,
            userId = userId,
            title = title,
            content = content
        )
    }
}
