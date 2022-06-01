package space.stanton.technicaltest.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PostMasterViewPagerAdapter(parentFragment: Fragment, private val childFragments: List<Fragment>): FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int {
        return childFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return childFragments[position]
    }

}