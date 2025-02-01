package com.example.mangashelfassignment.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface NetworkApi {

    @GET("/b/KEJO")
    suspend fun fetchManga(): Response<List<MangaDto>>
}