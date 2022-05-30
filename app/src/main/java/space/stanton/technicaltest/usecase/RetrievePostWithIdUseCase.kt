package space.stanton.technicaltest.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.repository.PostRepository
import java.io.IOException
import javax.inject.Inject

class RetrievePostWithIdUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(postId: Int): Flow<DataResource<Post>> {
        return flow<DataResource<Post>> {
            try {
                emit(DataResource.Waiting())
                val post = postRepository.retrievePostWithId(postId = postId)
                emit(
                    DataResource.Successful(
                        data = post,
                        operation = Operation.GET
                    )
                )
            } catch (e: HttpException) {
                emit(
                    DataResource.Failure(
                        operation = Operation.GET,
                        reason = NetworkFailureReason.UNKNOWN
                    )
                )
            } catch (e: IOException) {
                emit(
                    DataResource.Failure(
                        operation = Operation.GET,
                        reason = NetworkFailureReason.CONNECTION
                    )
                )
            }
        }
    }
}