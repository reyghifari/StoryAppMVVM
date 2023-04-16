package com.hann.storyapp.data.remote.response

data class StoryResponse(
    val error: Boolean,
    val listStory: List<StoryItem>,
    val message: String
)