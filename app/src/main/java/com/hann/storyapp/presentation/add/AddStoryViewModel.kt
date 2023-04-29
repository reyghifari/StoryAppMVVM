package com.hann.storyapp.presentation.add

import androidx.lifecycle.*
import com.hann.storyapp.data.Resource
import com.hann.storyapp.data.remote.response.AddStoryResponse
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.ui.preference.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyUseCase: StoryUseCase,
    private val pref: UserPreference
):ViewModel() {

    fun getUser() : LiveData<User>{
        return pref.getUser().asLiveData()
    }

    fun uploadStory(file : MultipartBody.Part, description: RequestBody, token: String,  lat : RequestBody?, lon: RequestBody?) : Flow<Resource<AddStoryResponse>> {
        return storyUseCase.uploadStories(file = file,description = description,token =token, lat = lat, lon = lon)
    }

}