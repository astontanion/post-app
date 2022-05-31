package space.stanton.technicaltest.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import space.stanton.technicaltest.repository.FakeCommentRepositoryImpl
import space.stanton.technicaltest.repository.FakePostRepositoryImpl
import space.stanton.technicaltest.repository.*

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