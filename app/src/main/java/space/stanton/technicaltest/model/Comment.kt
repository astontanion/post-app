package space.stanton.technicaltest.model

data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val content: String
)