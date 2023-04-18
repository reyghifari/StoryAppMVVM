package com.hann.storyapp.presentation.register

data class RegisterState (
    val isLoading : Boolean = false,
    val success: String = "",
    val error: String = ""
)