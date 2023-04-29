package com.hann.storyapp.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hann.storyapp.R
import com.hann.storyapp.databinding.ItemLayoutStoryBinding
import com.hann.storyapp.domain.model.Story
import java.util.*

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemLayoutStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(context: Context, story: Story) {
            binding.apply {
                textName.text = story.name
                textCreatedAt.text = story.createdAt
                Glide.with(context).load(story.photoUrl).into(imagePhoto)

                root.setOnClickListener {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(holder.itemView.context, story)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

}