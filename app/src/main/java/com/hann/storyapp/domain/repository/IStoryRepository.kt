package com.hann.storyapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.hann.storyapp.data.Resource
import com.hann.storyapp.data.remote.response.AddStoryResponse
import com.hann.storyapp.data.remote.response.LoginResult
import com.hann.storyapp.data.remote.response.RegisterResponse
import com.hann.storyapp.domain.model.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IStoryRepository {

    fun registerUser(username :String, email: String, password: String): Flow<Resource<RegisterResponse>>

    fun loginUser(email: String, password: String): Flow<Resource<LoginResult>>

    fun getAllStories(token :String): Flow<Resource<List<Story>>>

    fun getAllStoriesLocation(token :String): Flow<Resource<Flow<PagingData<Story>>>>

    fun getAllStoriesMap(location: Int, token :String): Flow<Resource<List<Story>>>

    fun uploadStories(file : MultipartBody.Part, description: RequestBody, token: String) : Flow<Resource<AddStoryResponse>>

}