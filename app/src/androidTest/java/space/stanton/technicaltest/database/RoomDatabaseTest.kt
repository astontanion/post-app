package space.stanton.technicaltest.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import space.stanton.technicaltest.repository.FakePostRepositoryImpl
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RoomDatabaseTest {

    private lateinit var context: Context
    private lateinit var db: AppDatabase
    private lateinit var postDao: PostDao

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        postDao = db.postDao()
    }

    @Test
    fun testAddingPostToDatabase() = runTest {
        val postRepository = FakePostRepositoryImpl(context)
        val post = postRepository.retrievePostWithId(postId = 86)
        postDao.savePost(post.toPostEntity())

        val postEntity = postDao.retrievePostWithId(postId = post.id)

        assertEquals(post, postEntity?.toPost())

    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

}