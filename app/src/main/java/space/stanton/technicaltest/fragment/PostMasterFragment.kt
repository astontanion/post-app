package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import space.stanton.technicaltest.R
import space.stanton.technicaltest.adapter.PostMasterViewPagerAdapter
import space.stanton.technicaltest.databinding.PostMasterFragmentBinding
import space.stanton.technicaltest.viewmodel.PostMasterViewModel

@AndroidEntryPoint
class PostMasterFragment: Fragment() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: PostMasterFragmentBinding
    private lateinit var navController: NavController
    private val postMasterViewModel: PostMasterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PostMasterFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@PostMasterFragment
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (binding.postMasterDetailFragmentContainer != null) {
            postMasterViewModel.lastSelectedPost.value?.let { post ->
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.post_master_detail_fragment_container,
                        PostDetailFragment::class.java,
                        bundleOf(PostDetailFragment.ARG_POST_ID to post.id)
                    ).commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.postMasterToolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )

        binding.postMasterViewPager.apply {
            adapter = PostMasterViewPagerAdapter(
                this@PostMasterFragment,
                listOf(
                    PostListFragment.getInstance(isOffline = false),
                    PostListFragment.getInstance(isOffline = true)
                )
            )
        }

        val tabs = resources.getStringArray(R.array.post_master_tabs)

        TabLayoutMediator(binding.postMasterTablayout, binding.postMasterViewPager) { tab, position ->
            tab.apply {
                text = tabs[position]
            }
        }.attach()

        postMasterViewModel.lastSelectedPost.observe(viewLifecycleOwner) { post ->
            post?.let {
                postMasterViewModel.onLastSelectedPostChange(null)
                if (binding.postMasterDetailFragmentContainer != null) {
                    childFragmentManager.beginTransaction()
                        .replace(
                            R.id.post_master_detail_fragment_container,
                            PostDetailFragment::class.java,
                            bundleOf(PostDetailFragment.ARG_POST_ID to post.id)
                        ).commit()
                } else {
                    navController.navigate(
                        R.id.action_postMasterFragment_to_postDetailFragment,
                        PostDetailFragment.buildBundle(it.id)
                    )
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            postMasterViewModel.badgeCount.collectLatest { count ->
                binding.postMasterTablayout.getTabAt(1)?.let { tab ->
                    tab.apply {
                        orCreateBadge.number = count
                    }
                }
            }
        }
    }
}