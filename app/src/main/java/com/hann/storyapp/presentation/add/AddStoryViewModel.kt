package com.hann.storyapp.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hann.storyapp.data.Resource
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.presentation.register.RegisterState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyUseCase: StoryUseCase
):ViewModel() {

    private val _state = MutableLiveData<AddStoryState>()
    val state : LiveData<AddStoryState> = _state

    fun uploadStories(file : MultipartBody.Part, description: RequestBody, token: String){
        storyUseCase.uploadStories(file,description,token).onEach {
            result ->
            when(result){
                is Resource.Loading -> {
                    _state.value = AddStoryState(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value  = AddStoryState(error = result.message ?: "Error unexpected")
                }
                is Resource.Success -> {
                    _state.value = result.data?.let { AddStoryState(success = it.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
}