package com.hann.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hann.storyapp.R
import com.hann.storyapp.databinding.ItemLayoutStoryBinding
import com.hann.storyapp.domain.model.Story
import java.util.ArrayList

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private var listData = ArrayList<Story>()
    var onItemClick: ((Story) -> Unit)? = null

    fun setData(newListData: List<Story>?) {
        if (newListData == null) return
        val diffResult = DiffUtil.calculateDiff(MyDiffUtil(listData, newListData))
        listData.clear()
        listData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_story, parent, false))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
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
                onItemClick?.invoke(listData[adapterPosition])
            }
        }

    }

    class MyDiffUtil(private val oldList: List<Story>, private val newList: List<Story>): DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}