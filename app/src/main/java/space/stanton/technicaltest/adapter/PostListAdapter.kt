package space.stanton.technicaltest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import space.stanton.technicaltest.BR
import space.stanton.technicaltest.R
import space.stanton.technicaltest.model.Post

class PostListAdapter(private val onItemClick: (post: Post) -> Unit) :
    RecyclerView.Adapter<PostListAdapter.PostViewHolder>() {

    private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val rootView  = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        val binding: ViewDataBinding? = DataBindingUtil.bind(rootView)
        return PostViewHolder(itemview = rootView, binding = binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(differ.currentList[position], onItemClick)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitItems(items: List<Post>) {
        differ.submitList(items)
    }

    class PostViewHolder(
        itemview: View,
        private val binding: ViewDataBinding?
    ) : RecyclerView.ViewHolder(itemview) {
        fun bind(post: Post, onItemClick: (post: Post) -> Unit) {
            itemView.setOnClickListener {
                onItemClick(post)
            }

            binding?.apply {
                setVariable(BR.post, post)
                executePendingBindings()
            }
        }
    }
}