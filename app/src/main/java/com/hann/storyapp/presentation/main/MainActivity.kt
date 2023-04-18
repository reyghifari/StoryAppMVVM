package com.hann.storyapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.hann.storyapp.databinding.ActivityMainBinding
import com.hann.storyapp.domain.model.User
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
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initRecyclerView()

        mainViewModel.getUser().observe(this){
            user = it
            binding.toolbar.tvName.text = it.name
        }

        mainViewModel.state.observe(this){
            if (it.isLoading){
                binding.shimmerLayoutMain.visibility = View.VISIBLE
                binding.shimmerLayoutMain.startShimmer()
                binding.rvStory.visibility = View.GONE
            }
            if (it.error.isNotBlank()){
                binding.rvStory.visibility = View.GONE
                binding.shimmerLayoutMain.visibility = View.GONE
                binding.viewErrorMain.root.visibility = View.VISIBLE
            }
            if (it.story.isNotEmpty()){
                binding.shimmerLayoutMain.stopShimmer()
                binding.shimmerLayoutMain.visibility = View.GONE
                binding.rvStory.visibility = View.VISIBLE
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
    override fun onResume() {
        super.onResume()
        mainViewModel.getAllStories(user.token)
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