package space.stanton.technicaltest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import space.stanton.technicaltest.model.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this)  {
                instance ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "space.stanton.technicaltest-database"
                ).build().also { instance = it }
            }
        }
    }
}