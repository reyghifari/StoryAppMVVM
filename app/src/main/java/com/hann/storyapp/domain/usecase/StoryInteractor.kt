package com.hann.storyapp.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.hann.storyapp.data.Resource
import com.hann.storyapp.data.remote.response.AddStoryResponse
import com.hann.storyapp.data.remote.response.LoginResult
import com.hann.storyapp.data.remote.response.RegisterResponse
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.domain.repository.IStoryRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryInteractor(private val iStoryRepository: IStoryRepository) : StoryUseCase {

    override fun registerUser( username: String, email: String,password: String ): Flow<Resource<RegisterResponse>> {
        return iStoryRepository.registerUser(username, email,password)
    }

    override fun loginUser(email: String, password: String): Flow<Resource<LoginResult>> {
        return iStoryRepository.loginUser(email,password)
    }

    override fun getAllStories(token: String): Flow<Resource<List<Story>>> {
        return iStoryRepository.getAllStories(token)
    }

    override fun getAllStoriesLocation(token: String): Flow<PagingData<Story>> {
        return iStoryRepository.getAllStoriesLocation(token)
    }

    override fun getAllStoriesMap(location: Int, token: String): Flow<Resource<List<Story>>> {
        return iStoryRepository.getAllStoriesMap(location, token)
    }

    override fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String
    ): Flow<Resource<AddStoryResponse>> {
        return iStoryRepository.uploadStories(file,description,token)
    }
}