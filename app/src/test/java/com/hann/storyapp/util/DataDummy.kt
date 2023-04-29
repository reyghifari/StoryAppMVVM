package com.hann.storyapp.util
import com.hann.storyapp.data.remote.response.AddStoryResponse
import com.hann.storyapp.domain.model.Story
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


object DataDummy {
     fun generateListStoryFakeData(): List<Story> {
        val fakeStoryDTO = ArrayList<Story>()

        for (i in 1..10) {
            val fakeStoryItem = Story(
                id = "$i",
                name = "name item $i",
                description = "key $i",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666969385959_OAqj0s4-.jpg",
                createdAt = "2022-10-28T15:03:05.963Z",
                lat = 12.0,
                lon = 10.2
            )

            fakeStoryDTO.add(fakeStoryItem)
        }

        return fakeStoryDTO
    }

    fun generateDummyFileUploadResponse(): AddStoryResponse {
        return AddStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        return MultipartBody.Part.create("text".toRequestBody())
    }


    fun generateDummyRequestBody(): RequestBody {
        return "text".toRequestBody("text/plain".toMediaType())
    }

    fun getTokenDummy() : String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVVEaFU3RmdOMC04RHlVaVgiLCJpYXQiOjE2ODI1OTc3NDR9.GhZ8OKFzOMnrhvwrlHRVAaBKdZMBnz7S_Xnet-60Jd8"
    }

}
