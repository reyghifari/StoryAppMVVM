package com.hann.storyapp.presentation.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.hann.storyapp.data.Resource
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.ui.preference.UserPreference
import com.hann.storyapp.utils.Constants
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val storyUseCase: StoryUseCase,
    savedStateHandle: SavedStateHandle,
    private val pref: UserPreference
) : ViewModel(){

    private val _state = MutableLiveData<MainState>()
    val state : LiveData<MainState> = _state


    val story : LiveData<PagingData<Story>> =
        savedStateHandle.get<String>(Constants.PARAM_TOKEN)
            ?.let { storyUseCase.getAllStoriesLocation(it).asLiveData() }!!

    init {
        savedStateHandle.get<String>(Constants.PARAM_TOKEN)?.let {
            getAllStories(it)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getUser() : LiveData<User>{
        return pref.getUser().asLiveData()
    }

     fun getAllStories(token: String){
        storyUseCase.getAllStories(token).onEach {
            result ->
            when(result){
             is Resource.Loading -> {
                _state.value = MainState(isLoading = true)
             }
             is Resource.Error -> {
                _state.value = MainState(error = result.message ?: "Error Get Data Stories")
             }
             is Resource.Success -> {
                _state.value = MainState(story = result.data ?: emptyList())
             }
            }
        }.launchIn(viewModelScope)
    }


}