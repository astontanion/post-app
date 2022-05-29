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

class RetrieveAllPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(): Flow<DataResource<List<Post>>> {
        return flow<DataResource<List<Post>>> {
            try {
                emit(DataResource.Waiting())
                val posts = postRepository.retrieveAllPosts()
                emit(
                    DataResource.Successful(
                        data = posts,
                        operation = Operation.GET
                    )
                )
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(
                    DataResource.Failure(
                        operation = Operation.GET,
                        reason = NetworkFailureReason.UNKNOWN
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
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