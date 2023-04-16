package com.hann.storyapp.presentation.add

data class AddStoryState (
    val isLoading : Boolean = false,
    val success: String = "",
    val error: String = ""
)