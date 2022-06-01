package space.stanton.technicaltest.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
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
    fun retrieveAllPost(): Flow<List<PostEntity>>
}