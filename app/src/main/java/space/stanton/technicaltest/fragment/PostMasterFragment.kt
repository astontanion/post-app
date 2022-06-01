package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Comment
import space.stanton.technicaltest.R
import space.stanton.technicaltest.adapter.PostListViewPagerAdapter
import space.stanton.technicaltest.databinding.PostListFragmentBinding
import space.stanton.technicaltest.viewmodel.PostMasterViewModel

@AndroidEntryPoint
class PostMasterFragment: Fragment() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: PostListFragmentBinding
    private val postMasterViewModel: PostMasterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appBarConfiguration = AppBarConfiguration(findNavController().graph)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostListFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@PostMasterFragment
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (binding.postListDetailFragmentContainer != null) {
            postMasterViewModel.lastSelectedPost.value?.let { post ->
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.post_list_detail_fragment_container,
                        PostDetailFragment::class.java,
                        bundleOf(PostDetailFragment.ARG_POST_ID to post.id)
                    ).commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postListToolbar.setupWithNavController(
            findNavController(),
            appBarConfiguration
        )

        binding.postListViewPager.apply {
            adapter = PostListViewPagerAdapter(
                this@PostMasterFragment,
                listOf(
                    RecyclerViewFragment.getInstance(isOffline = false),
                    RecyclerViewFragment.getInstance(isOffline = true)
                )
            )
        }

        val tabs = resources.getStringArray(R.array.post_master_tabs)

        TabLayoutMediator(binding.postListTablayout, binding.postListViewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        postMasterViewModel.lastSelectedPost.observe(viewLifecycleOwner, Observer { post ->
            post?.let {
                postMasterViewModel.onLastSelectedPostChange(null)
                if (binding.postListDetailFragmentContainer != null) {
                    childFragmentManager.beginTransaction()
                        .replace(
                            R.id.post_list_detail_fragment_container,
                            PostDetailFragment::class.java,
                            bundleOf(PostDetailFragment.ARG_POST_ID to post.id)
                        ).commit()
                } else {
                    findNavController().navigate(
                        R.id.action_postListFragment_to_postDetailFragment,
                        PostDetailFragment.buildBundle(it.id)
                    )
                }
            }
        })
    }
}