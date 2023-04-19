package com.hann.storyapp.presentation.map

import androidx.lifecycle.*
import com.hann.storyapp.data.Resource
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.presentation.main.MainState
import com.hann.storyapp.utils.Constants
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MapViewModel(
    private val storyUseCase: StoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableLiveData<MapsState>()
    val state : LiveData<MapsState> = _state

    init {
        savedStateHandle.get<String>(Constants.PARAM_TOKEN)?.let {
            getStoryMap( it)
        }
    }

    private fun getStoryMap(token:String){
        storyUseCase.getAllStoriesMap(1, token).onEach {
            result ->
            when(result){
                is Resource.Loading -> {
                    _state.value = MapsState(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value = MapsState(error = result.message ?: "Error Get Data Stories")
                }
                is Resource.Success -> {
                    _state.value = MapsState(story = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }

}