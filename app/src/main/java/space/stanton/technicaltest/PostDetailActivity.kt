package space.stanton.technicaltest

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import org.json.JSONObject
import space.stanton.technicaltest.databinding.ActivityMainBinding
import space.stanton.technicaltest.databinding.ActivityPostDetailsBinding
import space.stanton.technicaltest.model.Post

/**
 * Shows details of a post
 */
class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_details)

        val id = intent.getStringExtra("postId")

        Thread {
            ApiCalls.getPostById(id!!) {
                if (it.second != null) {
                    // TODO - handle error
                } else {
                    val post = JSONObject(it.first!!.string())
                    runOnUiThread {
                        binding.post = Gson().fromJson(post.toString(), Post::class.java)
                        this@PostDetailActivity.title = post.getString("title")
                    }
                }
            }

        }.start()
    }
}