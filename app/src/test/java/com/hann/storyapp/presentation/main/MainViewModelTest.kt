package com.hann.storyapp.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.ui.adapter.StoryPagingAdapter
import com.hann.storyapp.ui.preference.UserPreference
import com.hann.storyapp.util.*
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
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

    private lateinit var mainViewModel : MainViewModel

    @Mock private lateinit var storuUseCase: StoryUseCase
    @Mock private lateinit var savedStateHandle: SavedStateHandle
    @Mock private lateinit var userPreference: UserPreference

    @Before
    fun setUp(){
        mainViewModel = MainViewModel(storuUseCase, savedStateHandle, userPreference)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get list story success`() = runBlocking {
        val dataDummy = DataDummy.generateListStoryFakeData()
        val data = PagedTestDataSource.snapshot(dataDummy)

        val flow = flowOf(data)

        `when`(storuUseCase.getAllStoriesLocation(DataDummy.getTokenDummy())).thenReturn(flow)

        val actualStories = mainViewModel.getStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.MyDiffUtil,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        val expectedFirstStory = dataDummy[0]
        val actualFirstStory = differ.snapshot().first()


        assertEquals(expectedFirstStory, actualFirstStory) //Memastikan data pertama yang dikembalikan sesuai.
        Assert.assertNotNull(differ.snapshot()) // Memastikan data tidak null.
        assertEquals(10, differ.snapshot().size) // Memastikan jumlah data sesuai dengan yang diharapkan.
    }

    @Test
    fun `get story returns null`() = runBlocking {
        val dataDummy = DataDummy.generateListStoryEmpty()
        val data = PagedTestDataSource.snapshot(dataDummy)

        val flow = flowOf(data)

        `when`(storuUseCase.getAllStoriesLocation(DataDummy.getTokenDummy())).thenReturn(flow)

        val actualStories = mainViewModel.getStory(dummyToken).value

        assertNull(actualStories)
        assertEquals(null, actualStories)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}
