package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import space.stanton.technicaltest.R
import space.stanton.technicaltest.databinding.PostDetailFragmentBinding
import space.stanton.technicaltest.network.DataMessage
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.viewmodel.PostDetailViewModel

@AndroidEntryPoint
class PostDetailFragment: Fragment() {

    companion object {
        const val ARG_POST_ID = "post_id"
        fun buildBundle(postId: Int): Bundle {
            return bundleOf(ARG_POST_ID to postId)
        }
    }

    private var postId: Int = 0
    private val postDetailViewModel: PostDetailViewModel by viewModels()
    private lateinit var binding: PostDetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = PostDetailFragmentArgs.fromBundle(it)
            postId = args.postId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostDetailFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@PostDetailFragment
            viewModel = postDetailViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            postDetailViewModel.state.collectLatest { state ->
                when (val result = state.postResource) {
                    is DataResource.Idle -> {}
                    is DataResource.Waiting -> {}
                    is DataResource.Successful -> {}
                    is DataResource.Failure -> {
                        val messageId = when ((result.message as DataMessage.Failure).reason) {
                            NetworkFailureReason.UNKNOWN -> R.string.error_retrieve_post_detail
                            NetworkFailureReason.CONNECTION -> R.string.error_network_connetion
                        }

                        Snackbar.make(requireContext(), view, getString(messageId), Snackbar.LENGTH_LONG).apply {
                            setAction(R.string.action_retry) {
                                postDetailViewModel.retrievePostWithId(postId)
                            }
                            show()
                        }
                    }
                }
            }
        }

        postDetailViewModel.apply {
            seeCommentClick = {
                this@PostDetailFragment.findNavController().navigate(
                    R.id.action_postDetailFragment_to_commentListFragment,
                    CommentListFragment.buildBundle(postId = postId)
                )
            }
        }
    }
}