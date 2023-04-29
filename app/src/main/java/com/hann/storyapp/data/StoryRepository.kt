package com.hann.storyapp.data

import android.util.Log
import androidx.paging.*
import com.hann.storyapp.data.local.database.StoryDatabase
import com.hann.storyapp.data.remote.RemoteDataSource
import com.hann.storyapp.data.remote.network.ApiResponse
import com.hann.storyapp.data.remote.network.ApiService
import com.hann.storyapp.data.remote.response.AddStoryResponse
import com.hann.storyapp.data.remote.response.LoginResult
import com.hann.storyapp.data.remote.response.RegisterResponse
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.domain.repository.IStoryRepository
import com.hann.storyapp.utils.DataMapper
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException


class StoryRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val database: StoryDatabase,
    private val apiService: ApiService
) : IStoryRepository{

    override fun registerUser(username: String, email: String, password: String): Flow<Resource<RegisterResponse>> = flow {
        try {
            emit(Resource.Loading())
            when(val register = remoteDataSource.registerUser(username,email,password).first()){
                is ApiResponse.Success -> {
                    emit(Resource.Success(register.data))
                }
                is ApiResponse.Empty -> emit(Resource.Error("Register not found"))
                is ApiResponse.Error -> emit(Resource.Error(register.errorMessage))
            }
        }catch (e: HttpException){
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected Error Occurred"
                )
            )
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet server"))
        }
    }



    override fun loginUser(email: String, password: String): Flow<Resource<LoginResult>> = flow {
        try {
            emit(Resource.Loading())
            when(val login = remoteDataSource.loginUser(email,password).first()){
                is ApiResponse.Success -> {
                    emit(Resource.Success(login.data))
                }
                is ApiResponse.Empty -> emit(Resource.Error("Register not found"))
                is ApiResponse.Error -> emit(Resource.Error(login.errorMessage))
            }
        }catch (e: HttpException){
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected Error Occurred"
                )
            )
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet server"))
        }
    }

    override fun getAllStories(token: String): Flow<Resource<List<Story>>> = flow {
        try {
            emit(Resource.Loading())
            when(val listStory = remoteDataSource.getAllStories(token).first()){
                is ApiResponse.Success -> {
                    val data = DataMapper.mapResponsesToDomain(listStory.data)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> emit(Resource.Error("User not found"))
                is ApiResponse.Error -> emit(Resource.Error(listStory.errorMessage))
            }
        }catch (e: HttpException){
            emit(
                Resource.Error(e.localizedMessage ?: "An unexpected Error Occurred")
            )
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet server"))
        }
    }


    override fun getAllStoriesLocation(token: String):  Flow<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService,
                token
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).flow
    }


    override fun getAllStoriesMap(location: Int, token: String): Flow<Resource<List<Story>>> = flow {
        try {
            emit(Resource.Loading())
            when(val listStory = remoteDataSource.getAllStoriesMap(location,token).first()){
                is ApiResponse.Success -> {
                    val data = DataMapper.mapResponsesToDomain(listStory.data)
                    emit(Resource.Success(data))
                }
                is ApiResponse.Empty -> emit(Resource.Error("Data not found"))
                is ApiResponse.Error -> emit(Resource.Error(listStory.errorMessage))
            }

        }catch (e: HttpException){
            emit(
                Resource.Error(e.localizedMessage ?: "An unexpected Error Occurred")
            )
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet server"))
        }
    }

    override fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat : RequestBody?,
        lon: RequestBody?
    ): Flow<Resource<AddStoryResponse>> = flow {
        try {
            emit(Resource.Loading())
            when(val upload = remoteDataSource.uploadImage(file,description,token, lat, lon).first()){
                is ApiResponse.Success -> {
                    emit(Resource.Success(upload.data))
                }
                is ApiResponse.Empty -> emit(Resource.Error("Upload not found"))
                is ApiResponse.Error -> emit(Resource.Error(upload.errorMessage))
            }
        }catch (e: HttpException){
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected Error Occurred"
                )
            )
        }catch (e: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet server"))
        }
    }
}