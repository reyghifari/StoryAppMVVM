package com.hann.storyapp.data.remote.network


import com.hann.storyapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): StoryResponse

    @GET("stories")
    suspend fun getAllStoriesLocation(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ) : StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String,
        @Part("lat") lat: RequestBody?,
        @Part("lon") long: RequestBody?
    ) : AddStoryResponse

    @GET("stories")
    suspend fun getAllStoriesMap(
        @Query("location") location : Int,
        @Header("Authorization") token: String
    ): StoryResponse

}