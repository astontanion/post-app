package space.stanton.technicaltest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.stanton.technicaltest.network.ApiService
import space.stanton.technicaltest.repository.*

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providePostRepository(): PostRepository {
        return PostRepositoryImpl(ApiService<PostEndPoint>())
    }

    @Provides
    fun provideCommentRepository(): CommentRepository {
        return CommentRepositoryImpl(ApiService<CommentEndPoint>())
    }
}