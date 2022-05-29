package space.stanton.technicaltest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import org.json.JSONObject
import space.stanton.technicaltest.ApiCalls
import space.stanton.technicaltest.databinding.PostDetailFragmentBinding
import space.stanton.technicaltest.model.Post

class PostDetailFragment: Fragment() {

    companion object {
        fun buildBundle(postId: Int): Bundle {
            return bundleOf("post_id" to postId)
        }
    }

    private lateinit var binding: PostDetailFragmentBinding
    private var postId: Int = 0

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
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {
            ApiCalls.getPostById(postId) {
                if (it.second != null) {
                    // TODO - handle error
                } else {
                    val post = Gson().fromJson(it.first!!.string(), Post::class.java)
                    requireActivity().runOnUiThread {
                        binding.post = post
                        (requireActivity() as AppCompatActivity).title = post.title
                    }
                }
            }

        }.start()
    }
}