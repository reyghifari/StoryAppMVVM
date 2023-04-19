package com.hann.storyapp.presentation.map

import com.hann.storyapp.domain.model.Story

data class MapsState (
    val isLoading : Boolean = false,
    val story: List<Story> = emptyList(),
    val error: String = ""
)