package com.example.mangashelfassignment.data.util

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Date

class CustomDateAdapter {

    @ToJson
    @TypeConverter
    fun toJson(date: Date): Long {
        return date.time / 1000
    }

    @FromJson
    @TypeConverter
    fun fromJson(timestamp: Long): Date {
        return Date(timestamp * 1000)
    }
}