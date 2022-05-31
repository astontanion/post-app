package space.stanton.technicaltest.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.GenericFailureReason
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.repository.PostRepository
import javax.inject.Inject

class RetrieveAllSavedPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(): Flow<DataResource<List<Post>>> {
        return flow<DataResource<List<Post>>> {
            try {
                emit(DataResource.Waiting())
                val posts = postRepository.retrieveAllSavedPosts()
                emit(
                    DataResource.Successful(
                        data = posts,
                        operation = Operation.GET
                    )
                )
            } catch (e: Exception) {
                emit(
                    DataResource.Failure(
                        operation = Operation.GET,
                        reason = GenericFailureReason.UNKNOWN
                    )
                )
            }
        }
    }
}