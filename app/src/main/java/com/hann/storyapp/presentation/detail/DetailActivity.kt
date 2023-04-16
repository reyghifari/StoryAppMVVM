package com.hann.storyapp.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.hann.storyapp.databinding.ActivityDetailBinding
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.utils.Constants

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val story = intent.getParcelableExtra<Story>(Constants.EXTRA_STORY)

        if (story != null){
            binding.tvNameDetail.text = story.name
            binding.tvDateDetail.text = story.createdAt
            binding.tvDescDetail.text = story.description
            with(binding){
                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .into(ivImageDetail)
            }
        }

    }
}