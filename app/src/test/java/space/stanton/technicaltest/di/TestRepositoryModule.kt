package space.stanton.technicaltest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.stanton.technicaltest.repository.CommentRepository
import space.stanton.technicaltest.repository.FakeCommentRepositoryImpl
import space.stanton.technicaltest.repository.FakePostRepositoryImpl
import space.stanton.technicaltest.repository.PostRepository

@Module
@InstallIn(SingletonComponent::class)
class TestRepositoryModule {

    @Provides
    fun providePostRepository(): PostRepository {
        return FakePostRepositoryImpl()
    }

    @Provides
    fun provideCommentRepository(): CommentRepository {
        return FakeCommentRepositoryImpl()
    }
}