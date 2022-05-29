package space.stanton.technicaltest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import space.stanton.technicaltest.BR
import space.stanton.technicaltest.R
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