package space.stanton.technicaltest.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PostListViewPagerAdapter(parentFragment: Fragment, private val childFragments: List<Fragment>): FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int {
        return childFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return childFragments[position]
    }

}