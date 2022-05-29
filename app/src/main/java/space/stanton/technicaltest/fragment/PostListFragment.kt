package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import space.stanton.technicaltest.ApiCalls
import space.stanton.technicaltest.adapter.PostAdapter
import space.stanton.technicaltest.R
import space.stanton.technicaltest.databinding.PostListFragmentBinding
import space.stanton.technicaltest.model.Post

class PostListFragment: Fragment() {

    private lateinit var binding: PostListFragmentBinding

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
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).title = getString(R.string.activity_label)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Thread {
            ApiCalls.loadAll {
                if (it.second != null) {
                    //TODO - handle error
                } else {
                    requireActivity().runOnUiThread {
                        val itemType = object: TypeToken<List<Post>>(){}.type
                        val listOfPosts = Gson().fromJson<List<Post>>(it.first!!.string(), itemType)
                        binding.postsList.apply {
                            adapter = PostAdapter(listOfPosts, onItemClick = { id ->
                                this@PostListFragment.findNavController().navigate(
                                    R.id.action_postListFragment_to_postDetailFragment,
                                    PostDetailFragment.buildBundle(id)
                                )
                            })
                        }
                    }
                }
            }
        }.start()
    }
}