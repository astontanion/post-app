package space.stanton.technicaltest.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.repository.FakePostRepositoryImpl
import space.stanton.technicaltest.usecase.RetrievePostWithIdUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    lateinit var retrievePostWithIdUseCase: RetrievePostWithIdUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        retrievePostWithIdUseCase = RetrievePostWithIdUseCase(
            FakePostRepositoryImpl()
        )
    }

    @Test
    fun testThatTheFirstStateEmissionIsIdle() = runTest {
        testDispatcher.run {
            val viewModel = PostDetailViewModel(
                retrievePostWithIdUseCase,
                SavedStateHandle(mapOf("post_id" to 86))
            )
            val state = viewModel.state.first()
            assertTrue(state.postResource is DataResource.Idle)
        }
    }

    @Test
    fun testThatStateIsChanging() = runTest {
        testDispatcher.run {
            val viewModel = PostDetailViewModel(
                retrievePostWithIdUseCase,
                SavedStateHandle(mapOf("post_id" to 86))
            )
            // since we are collecting the lastest state,
            // we are only guaranted to receive two states: IDLE and (SUCCESS OR FAILURE)
            val stateList = viewModel.state.take(2).toList()
            assertFalse(stateList.last().postResource is DataResource.Idle)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}