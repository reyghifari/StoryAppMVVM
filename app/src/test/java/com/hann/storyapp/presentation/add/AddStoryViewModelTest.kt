package com.hann.storyapp.presentation.add

import com.hann.storyapp.data.Resource
import com.hann.storyapp.util.CoroutinesTestRule
import com.hann.storyapp.util.DataDummy
import junit.framework.Assert
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()


    @Mock
    private lateinit var addStoryViewModel : AddStoryViewModel

    private val dummyUploadResponse = DataDummy.generateDummyFileUploadResponse()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyToken = DataDummy.getTokenDummy()

    @Test
    fun `Upload file successfully`() = runTest {

            val expected = flowOf(Resource.Success(dummyUploadResponse))

            Mockito.`when`( addStoryViewModel.uploadStory(dummyMultipart, dummyDescription ,dummyToken, null, null))
                .thenReturn(expected)

            val actual = addStoryViewModel.uploadStory(dummyMultipart, dummyDescription ,dummyToken, null, null)
            actual.collect {
                    result ->
                when(result){
                    is Resource.Loading -> {

                    }
                    is Resource.Error -> {
                        Assert.assertFalse(result.data!!.error)
                    }
                    is Resource.Success -> {
                        Assert.assertNotNull(result.data)
                        Assert.assertTrue(true)
                        Assert.assertSame(dummyUploadResponse, result.data)
                    }
                }
            }
            Mockito.verify(addStoryViewModel)
                .uploadStory(dummyMultipart, dummyDescription ,dummyToken, null, null)
        }
}