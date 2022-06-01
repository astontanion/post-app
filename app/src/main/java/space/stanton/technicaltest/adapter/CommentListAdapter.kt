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
import space.stanton.technicaltest.model.Comment

class CommentListAdapter: RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.comment_item, parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comment = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitItems(items: List<Comment>) {
        differ.submitList(items)
    }

    class ViewHolder(
        itemView: View,
        private val binding: ViewDataBinding): RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) {
            binding.apply {
                setVariable(BR.comment, comment)
                executePendingBindings()
            }
        }
    }
}