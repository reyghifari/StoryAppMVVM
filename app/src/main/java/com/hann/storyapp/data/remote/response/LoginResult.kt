package com.hann.storyapp.data.remote.response

data class LoginResult(
    val name: String,
    val token: String,
    val userId: String
)