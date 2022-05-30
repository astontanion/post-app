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
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.repository.FakePostRepositoryImpl

@OptIn(ExperimentalCoroutinesApi::class)
class RetrieveAllPostUseCaseTest {

    private lateinit var retrieveAllPostUseCase: RetrieveAllPostUseCase

    @Before
    fun setup() {
        retrieveAllPostUseCase = RetrieveAllPostUseCase(FakePostRepositoryImpl())
    }

    @Test
    fun testThatRetrievingAllPostNeverEmitIdle() = runTest {
        val results = retrieveAllPostUseCase().take(2).toList()
        assertFalse(DataResource.Idle() in results)
    }

    @Test
    fun testThatRetrievingAllPostEmitWaitingFirst() = runTest {
        val waiting = retrieveAllPostUseCase().first()
        assertTrue(waiting is DataResource.Waiting)
    }

    @Test
    fun testThatRetrievingAllPostEmitAtLeaseTwoValues() = runTest {
        val result = retrieveAllPostUseCase().drop(1).first()
        assertNotEquals(DataResource.Waiting<List<Post>>(), result)
    }

    @Test
    fun testThatRetrievingAllPostDoesNotReturnNull() = runTest {
        val result = retrieveAllPostUseCase().drop(1).first()
        assertNotNull(result.data)
    }

    @Test
    fun testThatRetrievingAllPostDoesNotReturnAnEmptyList() = runTest {
        val results = retrieveAllPostUseCase().take(2).toList()
        assertNotEquals(listOf<Post>(), results[0].data)
    }
}