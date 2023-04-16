package com.hann.storyapp.presentation.main

import com.hann.storyapp.domain.model.Story

data class MainState (
    val isLoading : Boolean = false,
    val story: List<Story> = emptyList(),
    val error: String = ""
)