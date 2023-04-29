package com.hann.storyapp.presentation.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.ui.preference.UserPreference
import com.hann.storyapp.utils.Constants
import kotlinx.coroutines.launch

class MainViewModel(
    private val storyUseCase: StoryUseCase,
    savedStateHandle: SavedStateHandle,
    private val pref: UserPreference
) : ViewModel(){

    var token : String = ""

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    init {
        savedStateHandle.get<String>(Constants.PARAM_TOKEN)?.let {
            token = it
        }
    }

    fun getStory(token :String) : LiveData<PagingData<Story>> {
        return storyUseCase.getAllStoriesLocation(token).cachedIn(viewModelScope).asLiveData()
    }

    fun getUser() : LiveData<User>{
        return pref.getUser().asLiveData()
    }

}