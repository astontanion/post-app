package space.stanton.technicaltest.fragment

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import space.stanton.technicaltest.R
import space.stanton.technicaltest.di.RepositoryModule
import space.stanton.technicaltest.launchFragmentInHiltContainer
import space.stanton.technicaltest.repository.PostRepository
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var postRepository: PostRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testClickingOnSeeCommentsNavigateToCommentListFragment() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val postDetailFragmentArgs = PostDetailFragmentArgs(postId = 86).toBundle()

        launchFragmentInHiltContainer<PostDetailFragment>(navController, postDetailFragmentArgs) {
            navController.setCurrentDestination(R.id.postDetailFragment, postDetailFragmentArgs)
        }

        onView(withId(R.id.button_see_coments)).perform(click())

        assertEquals(R.id.commentListFragment ,navController.currentDestination?.id)
    }

    @Test
    fun testThatClickingOnSaveButtonSavesPostInDataBase() = runTest {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val postDetailFragmentArgs = PostDetailFragmentArgs(postId = 86).toBundle()

        launchFragmentInHiltContainer<PostDetailFragment>(navController, postDetailFragmentArgs) {
            navController.setCurrentDestination(R.id.postDetailFragment, postDetailFragmentArgs)
        }

        onView(withId(R.id.button_save_post)).perform(click())

        val post = postRepository.retrieveSavedPostWithId(postId = 86)

        assertNotNull(post)
    }
}