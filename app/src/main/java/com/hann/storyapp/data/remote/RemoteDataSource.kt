package com.hann.storyapp.data.remote

import android.util.Log
import com.hann.storyapp.data.remote.network.ApiResponse
import com.hann.storyapp.data.remote.network.ApiService
import com.hann.storyapp.data.remote.response.AddStoryResponse
import com.hann.storyapp.data.remote.response.LoginResult
import com.hann.storyapp.data.remote.response.RegisterResponse
import com.hann.storyapp.data.remote.response.StoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDataSource constructor(private val apiService: ApiService) {

    suspend fun registerUser(name : String, email: String, password: String): Flow<ApiResponse<RegisterResponse>> {
        return flow {
            try {
                val response = apiService.registerUser(name = name, email = email, password = password)
                if (!response.error){
                    Log.d("Register Remote", response.toString())
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Error("create an account failed"))
                }
            }catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("Remote Data Source", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loginUser(email: String, password: String): Flow<ApiResponse<LoginResult>> {
        return flow {
            try {
                val response = apiService.loginUser(email = email, password = password)
                val result = response.loginResult
                if (!response.error){
                    emit(ApiResponse.Success(result))
                }else{
                    emit(ApiResponse.Error("create an account failed"))
                }
            }catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("Remote Data Source", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllStories(token : String): Flow<ApiResponse<List<StoryItem>>> {
        return flow {
            try {
                val response = apiService.getAllStories("Bearer $token")
                val dataArray = response.listStory
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            }catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("Remote Data Source", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadImage(file : MultipartBody.Part, description: RequestBody, token: String): Flow<ApiResponse<AddStoryResponse>> {
        return flow {
            try {
                val response = apiService.uploadStory(file = file, description = description, token = "Bearer $token")
                if (!response.error){
                    Log.d("Upload Image Remote", response.toString())
                    emit(ApiResponse.Success(response))
                }else{
                    emit(ApiResponse.Error("create an account failed"))
                }
            }catch (e:Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("Remote Data Source", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}