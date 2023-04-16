package com.hann.storyapp.presentation.login

import com.hann.storyapp.data.remote.response.LoginResult

data class LoginState (
    val isLoading : Boolean = false,
    val success: LoginResult? = null,
    val error: String = ""
)