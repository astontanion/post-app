package space.stanton.technicaltest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val content: String
)