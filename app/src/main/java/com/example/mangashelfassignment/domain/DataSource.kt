package com.example.mangashelfassignment.domain

import com.example.mangashelfassignment.core.Resource
import com.example.mangashelfassignment.data.remote.MangaDto

interface DataSource {
    suspend fun fetchManga(): Resource<List<MangaDto>>
}