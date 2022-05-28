package space.stanton.technicaltest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import space.stanton.technicaltest.databinding.ActivityMainBinding
import space.stanton.technicaltest.model.Post

class PostAdapter(private val items: List<Post>, val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(
        itemview: View,
        private val binding: ViewDataBinding?
    ) : RecyclerView.ViewHolder(itemview) {
        fun bind(post: Post, onItemClick: (Int) -> Unit) {
            itemView.setOnClickListener {
                onItemClick(post.id)
            }

            binding?.apply {
                setVariable(BR.post, post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val rootView  = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        val binding: ViewDataBinding? = DataBindingUtil.bind(rootView)
        return PostViewHolder(itemview = rootView, binding = binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size

}

/**
 * Displays a list of posts
 */
class PostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@PostListActivity, R.layout.activity_main)

        Thread {
            ApiCalls.loadAll {
                if (it.second != null) {
                    //TODO - handle error
                } else {
                    runOnUiThread {
                        val itemType = object: TypeToken<List<Post>>(){}.type
                        val listOfPosts = Gson().fromJson<List<Post>>(it.first!!.string(), itemType)
                        findViewById<RecyclerView>(R.id.postsList).adapter =
                            PostAdapter(listOfPosts, onItemClick = { id ->
                                startActivity(
                                    Intent(this, PostDetailActivity::class.java)
                                        .putExtra("postId", id)
                                )
                            })
                    }
                }
            }
        }.start()

    }
}

