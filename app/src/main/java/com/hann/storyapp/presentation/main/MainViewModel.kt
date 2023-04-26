package com.hann.storyapp.presentation.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.hann.storyapp.data.Resource
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.presentation.add.AddStoryState
import com.hann.storyapp.ui.preference.UserPreference
import com.hann.storyapp.utils.Constants
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val storyUseCase: StoryUseCase,
    savedStateHandle: SavedStateHandle,
    private val pref: UserPreference
) : ViewModel(){


    private val _state = MutableStateFlow(MainState())
    val state : StateFlow<MainState> = _state

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    init {
        savedStateHandle.get<String>(Constants.PARAM_TOKEN)?.let {
            getAllStories(it)
        }
    }

    private fun getAllStories(token: String){
        storyUseCase.getAllStoriesLocation(token).onEach {
                result ->
            when(result){
                is Resource.Loading -> {
                    _state.update {
                        it.copy(isLoading = true, isSuccess = false, isError = false)
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(error = result.message ?: "Error unexpected",  isSuccess = false, isLoading = false, isError = true)
                    }
                }
                is Resource.Success -> {
                    result.data?.collectLatest { pagingData ->
                        _state.update {
                            it.copy(story = pagingData, isSuccess = true, isLoading = false, isError = false)
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUser() : LiveData<User>{
        return pref.getUser().asLiveData()
    }



}