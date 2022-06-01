package space.stanton.technicaltest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.stanton.technicaltest.database.PostDao
import space.stanton.technicaltest.network.ApiService
import space.stanton.technicaltest.repository.*

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providePostRepository(postDao: PostDao): PostRepository {
        return PostRepositoryImpl(
            ApiService<PostEndPoint>(),
            postDao
        )
    }

    @Provides
    fun provideCommentRepository(): CommentRepository {
        return CommentRepositoryImpl(ApiService<CommentEndPoint>())
    }
}