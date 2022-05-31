package space.stanton.technicaltest.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import space.stanton.technicaltest.model.Comment
import space.stanton.technicaltest.network.DataMessage
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.repository.FakeCommentRepositoryImpl

@OptIn(ExperimentalCoroutinesApi::class)
class RetrieveAllCommentForPostUseCaseTest {

    private lateinit var retrieveAllCommentForPostUseCase: RetrieveAllCommentForPostUseCase

    @Before
    fun setup() {
        retrieveAllCommentForPostUseCase = RetrieveAllCommentForPostUseCase(
            FakeCommentRepositoryImpl()
        )
    }

    @Test
    fun testThatRetrievingAllCommentNeverEmitIdle() = runTest {
        val results = retrieveAllCommentForPostUseCase(postId = 1).take(2).toList()
        assertFalse(DataResource.Idle() in results)
    }

    @Test
    fun testThatRetrievingAllCommentEmitWaitingFirst() = runTest {
        val waiting = retrieveAllCommentForPostUseCase(postId = 1).first()
        assertTrue(waiting is DataResource.Waiting)
    }

    @Test
    fun testThatRetrievingAllCommentEmitAtLeaseTwoValues() = runTest {
        val result = retrieveAllCommentForPostUseCase(postId = 1).drop(1).first()
        assertNotEquals(DataResource.Waiting<List<Comment>>(), result)
    }

    @Test
    fun testThatRetrievingAllCommentDoesNotReturnNull() = runTest {
        val result = retrieveAllCommentForPostUseCase(postId = 1).drop(1).first()
        assertNotNull(result.data)
    }

    @Test
    fun testThatRetrievingAllCommentDoesNotReturnAnEmptyList() = runTest {
        val results = retrieveAllCommentForPostUseCase(postId = 1).take(2).toList()
        assertNotEquals(listOf<Comment>(), results[0].data)
    }

    @Test
    fun testThatRetrievingAllCommentReturnUnknownError() = runTest {
        val result = retrieveAllCommentForPostUseCase(postId = 2).drop(1).first()

        val message = result.message

        assertTrue(message is DataMessage.Failure)

        val reason = (message as DataMessage.Failure).reason

        assertEquals(NetworkFailureReason.UNKNOWN, reason)
    }

    @Test
    fun testThatRetrievingAPostDoesReturnConnectionError() = runTest {
        val result = retrieveAllCommentForPostUseCase(postId = -1).drop(1).first()

        val message = result.message

        assertTrue(message is DataMessage.Failure)

        val reason = (message as DataMessage.Failure).reason

        assertEquals(NetworkFailureReason.CONNECTION, reason)
    }
}