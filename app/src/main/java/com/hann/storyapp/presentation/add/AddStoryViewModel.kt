package com.hann.storyapp.presentation.add

import androidx.lifecycle.*
import com.hann.storyapp.data.Resource
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.ui.preference.UserPreference
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyUseCase: StoryUseCase,
    private val pref: UserPreference
):ViewModel() {

    private val _state = MutableLiveData<AddStoryState>()
    val state : LiveData<AddStoryState> = _state

    fun getUser() : LiveData<User>{
        return pref.getUser().asLiveData()
    }

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