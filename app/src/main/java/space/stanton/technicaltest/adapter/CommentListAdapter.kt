package space.stanton.technicaltest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import space.stanton.technicaltest.BR
import space.stanton.technicaltest.R
import space.stanton.technicaltest.model.Comment

class CommentListAdapter(
    private var items: List<Comment>): RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.comment_item, parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comment = items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addComments(items: List<Comment>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val itemView: View,
        private val binding: ViewDataBinding): RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) {
            binding.apply {
                setVariable(BR.comment, comment)
                executePendingBindings()
            }
        }
    }
}