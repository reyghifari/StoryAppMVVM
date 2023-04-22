package com.hann.storyapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hann.storyapp.databinding.ActivityMainBinding
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.presentation.add.AddStoryActivity
import com.hann.storyapp.presentation.detail.DetailActivity
import com.hann.storyapp.presentation.login.LoginActivity
import com.hann.storyapp.presentation.map.MapActivity
import com.hann.storyapp.ui.adapter.LoadingStateAdapter
import com.hann.storyapp.ui.adapter.StoryAdapter
import com.hann.storyapp.ui.adapter.StoryPagingAdapter
import com.hann.storyapp.utils.Constants
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding : ActivityMainBinding
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel.getUser().observe(this){
            user = it
            binding.toolbar.tvName.text = it.name
        }

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        getData()

        binding.btnFloatAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.ivMaps.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            intent.putExtra(Constants.PARAM_TOKEN, user.token)
            startActivity(intent)
        }

        binding.toolbar.ivLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.toolbar.ivLogout.setOnClickListener {
            mainViewModel.logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getData() {
        val adapter = StoryPagingAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.onItemClick = {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_STORY, it)
            startActivity(intent)
        }
        mainViewModel.story.observe(this){
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getAllStories(user.token)
    }

}