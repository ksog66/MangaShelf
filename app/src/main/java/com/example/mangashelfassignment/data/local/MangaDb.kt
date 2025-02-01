package com.example.mangashelfassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mangashelfassignment.data.local.dao.MangaDao
import com.example.mangashelfassignment.data.local.entity.MangaEntity
import com.example.mangashelfassignment.data.util.CustomDateAdapter

@Database(
    entities = [MangaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CustomDateAdapter::class
)
abstract class MangaDb : RoomDatabase() {

    abstract fun mangaDao(): MangaDao

}