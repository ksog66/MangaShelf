package com.example.mangashelfassignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class MangaEntity(
    @PrimaryKey
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedDate: Date,
    val category: String,
    val isFavorite: Boolean,
    val isRead: Boolean
)