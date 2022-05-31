package space.stanton.technicaltest.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.GenericFailureReason
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.repository.PostRepository
import javax.inject.Inject

class SavePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(post: Post): Flow<DataResource<Post>> {
        return flow<DataResource<Post>> {
            try {
                emit(DataResource.Waiting())
                val result = postRepository.savePost(post = post)
                emit(
                    DataResource.Successful(
                        operation = Operation.POST,
                        data = result
                    )
                )
            } catch (e: Exception) {
                emit(
                    DataResource.Failure(
                        operation = Operation.POST,
                        reason = GenericFailureReason.UNKNOWN
                    )
                )
            }
        }
    }
}