package com.example.mangashelfassignment.data.remote

import com.example.mangashelfassignment.core.DispatcherProvider
import com.example.mangashelfassignment.core.Resource
import com.example.mangashelfassignment.domain.DataSource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val api: NetworkApi
) : DataSource {

    override suspend fun fetchManga(): Resource<List<MangaDto>> =
        withContext(dispatchers.io) {
        return@withContext try {
            val result = api.fetchManga()
            if (result.isSuccessful) {
                val mangaList = result.body()
                Resource.Success(data = mangaList)
            } else {
                Resource.Success(null)
            }
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }
}