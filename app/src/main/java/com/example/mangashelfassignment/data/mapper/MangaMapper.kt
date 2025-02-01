package com.example.mangashelfassignment.data.mapper

import com.example.mangashelfassignment.data.local.entity.MangaEntity
import com.example.mangashelfassignment.data.remote.MangaDto
import com.example.mangashelfassignment.presentation.Manga


fun MangaDto.toMangaEntity(): MangaEntity {
    return MangaEntity(
        id = id,
        image = image,
        score = score,
        popularity = popularity,
        title = title,
        publishedDate = publishedChapterDate,
        category = category,
        isFavorite = false,
        isRead = false
    )
}

fun MangaEntity.toManga(): Manga {
    return Manga(
        id = id,
        image = image,
        score = score,
        popularity = popularity,
        title = title,
        publishedDate = publishedDate,
        category = category,
        isFavorite = isFavorite,
        isRead = isRead
    )
}