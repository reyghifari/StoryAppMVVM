package com.hann.storyapp.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.hann.storyapp.domain.model.Story
import com.hann.storyapp.ui.adapter.StoryPagingAdapter
import com.hann.storyapp.util.*
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()



    private val dummyToken = DataDummy.getTokenDummy()

    @Mock
    private lateinit var mainViewModel : MainViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get list story success`() = runBlocking {
        val dataDummy = DataDummy.generateListStoryFakeData()
        val data = PagedTestDataSource.snapshot(dataDummy)

        val stories  =MutableLiveData<PagingData<Story>>()
        stories.value = data

        Mockito.`when`(mainViewModel.getStory(dummyToken)).thenReturn(stories)

        val actualStories = mainViewModel.getStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.MyDiffUtil,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        Mockito.verify(mainViewModel).getStory(dummyToken)
        Assert.assertNotNull(differ.snapshot())
        assertEquals(10, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}
