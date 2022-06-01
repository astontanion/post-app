package space.stanton.technicaltest.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.repository.PostRepository
import javax.inject.Inject

class RetrieveAllSavedPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(): Flow<DataResource<List<Post>>> {
        return postRepository.retrieveAllSavedPosts().map { posts ->
            DataResource.Successful(
                data = posts,
                operation = Operation.GET
            )
        }
    }
}