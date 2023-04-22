package com.hann.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hann.storyapp.R
import com.hann.storyapp.databinding.ItemLayoutStoryBinding
import com.hann.storyapp.domain.model.Story

class StoryPagingAdapter : PagingDataAdapter<Story, StoryPagingAdapter.ViewHolder>(MyDiffUtil) {

    var onItemClick: ((Story) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_story, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemLayoutStoryBinding.bind(itemView)

        fun bind(data: Story) {
            with(binding){
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imagePhoto)
                textName.text = data.name
                textCreatedAt.text = data.createdAt
            }
        }

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(getItem(position)!!)
                }
            }
        }

    }

    object MyDiffUtil : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }
}