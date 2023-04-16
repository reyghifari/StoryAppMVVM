package com.hann.storyapp.presentation.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hann.storyapp.data.Resource
import com.hann.storyapp.data.remote.response.RegisterResponse
import com.hann.storyapp.domain.usecase.StoryUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {

    private val _state = MutableLiveData<RegisterState>()
    val state : LiveData<RegisterState> = _state

    fun registerUser(username: String, email: String, password: String){
            storyUseCase.registerUser(username, email, password).onEach {
                result ->
                when(result){
                    is Resource.Loading -> {
                        Log.d("Register ViewModel Load", result.data.toString())
                        _state.value = RegisterState(isLoading = true)
                    }
                    is Resource.Error -> {
                        Log.d("Register ViewModel Load", result.data.toString())
                        _state.value = RegisterState(error = result.message ?: "Failed create an account")
                    }
                    is Resource.Success -> {
                        Log.d("Register ViewModel Load", result.data.toString())
                        _state.value = result.data?.let { RegisterState(success = it.message) }
                    }
                }
            }.launchIn(viewModelScope)
    }


}

