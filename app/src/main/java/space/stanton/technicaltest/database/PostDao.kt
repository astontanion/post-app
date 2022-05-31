package space.stanton.technicaltest.database

import androidx.room.*
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.model.PostEntity

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePost(postEntity: PostEntity): Long

    @Delete
    suspend fun deletePostWithId(postEntity: PostEntity): Int

    @Query(value = "select * from post where id = :postId")
    suspend fun retrievePostWithId(postId: Int): PostEntity?

    @Query(value = "select * from post")
    suspend fun retrieveAllPost(): List<PostEntity>
}