package space.stanton.technicaltest.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.GenericFailureReason
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.repository.PostRepository
import javax.inject.Inject

class RetrieveSavedPostWithIdUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(postId: Int): Flow<DataResource<Post>> {
        return flow<DataResource<Post>> {
            try {
                emit(DataResource.Waiting())
                val post = postRepository.retrieveSavedPostWithId(postId = postId)
                emit(
                    DataResource.Successful(
                        data = post,
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