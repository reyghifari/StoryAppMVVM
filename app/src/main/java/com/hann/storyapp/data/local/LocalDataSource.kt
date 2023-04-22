package com.hann.storyapp.data.local

import androidx.paging.PagingSource
import com.hann.storyapp.data.local.dao.RemoteKeysDao
import com.hann.storyapp.data.local.dao.StoryDao
import com.hann.storyapp.data.local.entity.RemoteKeys
import com.hann.storyapp.domain.model.Story

class LocalDataSource constructor(private val storyDao: StoryDao, private val remoteKeysDao: RemoteKeysDao) {


    suspend fun insertAll(remoteKey: List<RemoteKeys>) = remoteKeysDao.insertAll(remoteKey)

    suspend fun getRemoteKeysId(id: String) : RemoteKeys? = remoteKeysDao.getRemoteKeysId(id)

    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteRemoteKeys()

    suspend fun insertStory(story: List<Story>) = storyDao.insertStory(story)

    fun getAllStory(): PagingSource<Int, Story> = storyDao.getAllStory()

    suspend fun deleteAll() = storyDao.deleteAll()
}