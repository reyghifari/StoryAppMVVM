package com.hann.storyapp.presentation.main

import androidx.paging.PagingData
import com.hann.storyapp.domain.model.Story

data class MainState (
    val isLoading : Boolean = false,
    val story: PagingData<Story> = PagingData.empty(),
    val error: String = "",
    val isError : Boolean = false,
    val isSuccess : Boolean = false,
)