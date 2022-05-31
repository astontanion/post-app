package space.stanton.technicaltest.model

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val content: String
) {
    fun toPostEntity(): PostEntity{
        return PostEntity(
            id = id,
            userId = userId,
            title = title,
            content = content
        )
    }
}
