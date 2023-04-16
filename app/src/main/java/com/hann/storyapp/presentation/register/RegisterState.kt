package com.hann.storyapp.presentation.register

import com.hann.storyapp.data.remote.response.RegisterResponse

data class RegisterState (
    val isLoading : Boolean = false,
    val success: String = "",
    val error: String = ""
)