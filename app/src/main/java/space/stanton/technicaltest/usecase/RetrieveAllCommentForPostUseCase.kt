package space.stanton.technicaltest.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import space.stanton.technicaltest.model.Comment
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.network.Operation
import space.stanton.technicaltest.repository.CommentRepository
import java.io.IOException
import javax.inject.Inject

class RetrieveAllCommentForPostUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {

    suspend operator fun invoke(postId: Int): Flow<DataResource<List<Comment>>> {
        return flow<DataResource<List<Comment>>> {
            try {
                emit(DataResource.Waiting())
                val post = commentRepository.retrieveCommentForPostWithId(postId = postId)
                emit(
                    DataResource.Successful(
                        operation = Operation.GET,
                        data = post
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