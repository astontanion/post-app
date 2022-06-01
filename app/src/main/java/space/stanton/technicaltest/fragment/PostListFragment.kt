package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import space.stanton.technicaltest.R
import space.stanton.technicaltest.adapter.PostListAdapter
import space.stanton.technicaltest.databinding.PostListFragmentBinding
import space.stanton.technicaltest.network.DataMessage
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.GenericFailureReason
import space.stanton.technicaltest.viewmodel.PostListViewModel
import space.stanton.technicaltest.viewmodel.PostMasterViewModel

@AndroidEntryPoint
class PostListFragment: Fragment() {

    private lateinit var binding: PostListFragmentBinding
    private val postListViewmodel: PostListViewModel by viewModels()
    private val postMasterViewModel: PostMasterViewModel by viewModels({ requireParentFragment() })
    private var isForOffline = false

    companion object {
        const val ARG_IS_FOR_OFFLINE = "is_for_offline"

        fun getInstance(isOffline: Boolean): PostListFragment {
            return PostListFragment().apply {
                arguments = bundleOf(ARG_IS_FOR_OFFLINE to isOffline)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            isForOffline =  args.getBoolean(ARG_IS_FOR_OFFLINE, false)
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postListAdapter = PostListAdapter { post ->
            postMasterViewModel.onLastSelectedPostChange(post)
        }

        binding.recyclerView.apply {
            adapter = postListAdapter
        }

        lifecycleScope.launchWhenCreated {
            postListViewmodel.state.collectLatest { state ->
                when (val result = state.postResource) {
                    is DataResource.Idle -> {}
                    is DataResource.Waiting -> {}
                    is DataResource.Successful -> {
                        result.data?.let { posts ->
                            postListAdapter.submitItems(posts)
                        }
                    }
                    is DataResource.Failure -> {
                        handleResourceFailure(result.message, view)
                    }
                }
            }
        }
    }

    private fun handleResourceFailure(
        message: DataMessage,
        view: View
    ) {
        val messageId = when ((message as DataMessage.Failure).reason) {
            GenericFailureReason.UNKNOWN -> R.string.error_retrieve_all_posts
            GenericFailureReason.CONNECTION -> R.string.error_network_connetion
        }

        Snackbar.make(requireContext(), view, getString(messageId), Snackbar.LENGTH_LONG).apply {
            setAction(R.string.action_retry) {
                postListViewmodel.retrieveAllPosts(isForOffline)
            }
            show()
        }
    }
}