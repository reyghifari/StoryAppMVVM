package com.hann.storyapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hann.storyapp.data.local.dao.RemoteKeysDao
import com.hann.storyapp.data.local.dao.StoryDao
import com.hann.storyapp.data.local.entity.RemoteKeys
import com.hann.storyapp.domain.model.Story

@Database(entities = [Story::class, RemoteKeys::class], version = 1, exportSchema = true)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao() : RemoteKeysDao

}