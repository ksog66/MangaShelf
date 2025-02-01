package com.example.mangashelfassignment.presentation

import java.util.Date

data class Manga(
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