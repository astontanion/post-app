package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import space.stanton.technicaltest.R
import space.stanton.technicaltest.adapter.CommentListAdapter
import space.stanton.technicaltest.databinding.CommentListFragmentBinding
import space.stanton.technicaltest.network.DataMessage
import space.stanton.technicaltest.network.DataResource
import space.stanton.technicaltest.network.NetworkFailureReason
import space.stanton.technicaltest.viewmodel.CommentListViewModel

@AndroidEntryPoint
class CommentListFragment: Fragment() {

    private lateinit var binding: CommentListFragmentBinding
    private val commentListViewModel: CommentListViewModel by viewModels()
    private var postId: Int = 0

    companion object {
        const val ARG_POST_ID = "post_id"
        fun buildBundle(postId: Int): Bundle {
            return bundleOf(PostDetailFragment.ARG_POST_ID to postId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = CommentListFragmentArgs.fromBundle(it)
            postId = args.postId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentListFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@CommentListFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val commentListAdapter = CommentListAdapter(listOf())

        binding.commentRecyclerView.apply {
            // todo add a recyclerview item decoration
            adapter = commentListAdapter
        }

        lifecycleScope.launchWhenCreated {
            commentListViewModel.state.collectLatest { state ->
                when(val result = state.commentResource) {
                    is DataResource.Idle -> {}
                    is DataResource.Waiting -> {}
                    is DataResource.Successful -> {
                        val comments = result.data!!
                        commentListAdapter.addComments(comments)
                    }
                    is DataResource.Failure -> {
                        val messageId = when ((result.message as DataMessage.Failure).reason) {
                            NetworkFailureReason.UNKNOWN -> R.string.error_retrieve_all_comment
                            NetworkFailureReason.CONNECTION -> R.string.error_network_connetion
                        }

                        Snackbar.make(requireContext(), view, getString(messageId), Snackbar.LENGTH_LONG).apply {
                            setAction(R.string.action_retry) {
                                commentListViewModel.retrieveAllCommentForPostWithId(postId = postId)
                            }
                            show()
                        }
                    }
                }
            }
        }
    }
}