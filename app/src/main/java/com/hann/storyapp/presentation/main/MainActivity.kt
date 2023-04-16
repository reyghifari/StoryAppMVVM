package com.hann.storyapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.hann.storyapp.databinding.ActivityMainBinding
import com.hann.storyapp.presentation.add.AddStoryActivity
import com.hann.storyapp.presentation.detail.DetailActivity
import com.hann.storyapp.presentation.login.LoginActivity
import com.hann.storyapp.ui.adapter.StoryAdapter
import com.hann.storyapp.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initRecyclerView()

        mainViewModel.state.observe(this){
            if (it.isLoading){
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
            if (it.error.isNotBlank()){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            if (it.story.isNotEmpty()){
                storyAdapter.setData(it.story)
            }
        }

        binding.toolbar.ivAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolbar.ivLogout.setOnClickListener {
            mainViewModel.logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initRecyclerView() {
        storyAdapter = StoryAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvStory.adapter = storyAdapter
        binding.rvStory.setHasFixedSize(false)
        storyAdapter.onItemClick = {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_STORY, it)
            startActivity(intent)
        }
    }
}