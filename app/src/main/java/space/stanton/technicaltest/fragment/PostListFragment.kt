package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import space.stanton.technicaltest.adapter.PostAdapter
import space.stanton.technicaltest.R
import space.stanton.technicaltest.databinding.PostListFragmentBinding
import space.stanton.technicaltest.network.DataMessage
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.viewmodel.PostListViewModel

@AndroidEntryPoint
class PostListFragment: Fragment() {

    private lateinit var binding: PostListFragmentBinding

    private val postListViewmodel: PostListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostListFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@PostListFragment
            viewModel = postListViewmodel
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).title = getString(R.string.activity_label)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            postListViewmodel.state.collectLatest { state ->
                when (val result = state.postResource) {
                    is DataResource.Idle -> {}
                    is DataResource.Waiting -> {}
                    is DataResource.Successful -> {
                        result.data?.let { posts ->
                            binding.postsList.apply {
                                adapter = PostAdapter(posts, onItemClick = { id ->
                                    this@PostListFragment.findNavController().navigate(
                                        R.id.action_postListFragment_to_postDetailFragment,
                                        PostDetailFragment.buildBundle(id)
                                    )
                                })
                            }
                        }
                    }
                    is DataResource.Failure -> {
                        val messageId = when ((result.message as DataMessage.Failure).reason) {
                            NetworkFailureReason.UNKNOWN -> R.string.error_retrieve_all_posts
                            NetworkFailureReason.CONNECTION -> R.string.error_network_connetion
                        }

                        Snackbar.make(requireContext(), view, getString(messageId), Snackbar.LENGTH_LONG).apply {
                            setAction(R.string.action_retry) {
                                postListViewmodel.retrieveAllPosts()
                            }
                            show()
                        }
                    }
                }
            }
        }
    }
}