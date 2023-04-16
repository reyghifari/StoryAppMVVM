package com.hann.storyapp.domain.model

data class User(
    val name: String,
    val token: String,
    val isLogin: Boolean
)