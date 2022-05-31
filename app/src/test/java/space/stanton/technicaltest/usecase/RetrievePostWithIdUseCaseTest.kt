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
import space.stanton.technicaltest.model.Post
import space.stanton.technicaltest.network.DataMessage
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.repository.FakePostRepositoryImpl

@OptIn(ExperimentalCoroutinesApi::class)
class RetrievePostWithIdUseCaseTest {

    private lateinit var retrievePostWithIdUseCase: RetrievePostWithIdUseCase

    @Before
    fun setup() {
        retrievePostWithIdUseCase = RetrievePostWithIdUseCase(FakePostRepositoryImpl())
    }

    @Test
    fun testThatRetrievingPostWithIdNeverEmitIdle() = runTest {
        val results = retrievePostWithIdUseCase(86).take(2).toList()
        assertFalse(DataResource.Idle() in results)
    }

    @Test
    fun testThatRetrievingPostWithIdEmitWaitingFirst() = runTest {
        val waiting = retrievePostWithIdUseCase(86).first()
        assertTrue(waiting is DataResource.Waiting)
    }

    @Test
    fun testThatRetrievingPostWithIdEmitAtLeaseTwoValues() = runTest {
        val result = retrievePostWithIdUseCase(86).drop(1).first()
        assertNotEquals(DataResource.Waiting<List<Post>>(), result)
    }

    @Test
    fun testThatRetrievingAPostDoesNotReturnNull() = runTest {
        val result = retrievePostWithIdUseCase(86).drop(1).first()
        assertNotNull(result.data)
    }

    @Test
    fun testThatRetrievingAPostDoesReturnUnknownError() = runTest {
        val result = retrievePostWithIdUseCase(1).drop(1).first()

        val message = result.message

        assertTrue(message is DataMessage.Failure)

        val reason = (message as DataMessage.Failure).reason

        assertEquals(NetworkFailureReason.UNKNOWN, reason)
    }

    @Test
    fun testThatRetrievingAPostDoesReturnConnectionError() = runTest {
        val result = retrievePostWithIdUseCase(-1).drop(1).first()

        val message = result.message

        assertTrue(message is DataMessage.Failure)

        val reason = (message as DataMessage.Failure).reason

        assertEquals(NetworkFailureReason.CONNECTION, reason)
    }

    @Test
    fun testRetrievingPostWithId86HasTheCorrectTitle() = runTest {
        val result = retrievePostWithIdUseCase(86).drop(1).first()
        val title = result.data?.title
        assertEquals("placeat quia et porro iste", title)
    }
}