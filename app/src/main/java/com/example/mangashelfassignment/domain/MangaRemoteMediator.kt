package com.example.mangashelfassignment.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mangashelfassignment.core.Resource
import com.example.mangashelfassignment.data.local.MangaDb
import com.example.mangashelfassignment.data.local.entity.MangaEntity

@OptIn(ExperimentalPagingApi::class)
class MangaRemoteMediator(
    private val dataSource: DataSource,
    private val db: MangaDb
) : RemoteMediator<Int, MangaEntity>() {

    private var hasMoreData = true
    private var hasNetworkDataLoaded = false

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MangaEntity>
    ): MediatorResult {
        return try {
            if (!hasMoreData) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            if (loadType == LoadType.PREPEND) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            if (loadType == LoadType.REFRESH) {
                hasNetworkDataLoaded = false
            }

            if (!hasNetworkDataLoaded) {
                when (val apiResponse = dataSource.fetchManga()) {
                    is Resource.Error -> {
                        val localData = db.mangaDao().getMangaCount()
                        if (localData > 0) {
                            return MediatorResult.Success(endOfPaginationReached = false)
                        }
                        return MediatorResult.Error(apiResponse.e)
                    }
                    is Resource.Success -> {
                        val existingManga = db.mangaDao().getAllMangaWithFlags().associateBy { it.id }
                        val mangas = apiResponse.data?.map { dto ->
                            val existing = existingManga[dto.id]
                            MangaEntity(
                                id = dto.id,
                                image = dto.image,
                                score = dto.score,
                                popularity = dto.popularity,
                                title = dto.title,
                                publishedDate = dto.publishedChapterDate,
                                category = dto.category,
                                isFavorite = existing?.isFavorite ?: false,
                                isRead = existing?.isRead ?: false
                            )
                        } ?: emptyList()

                        db.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                db.mangaDao().clearAll()
                            }
                            db.mangaDao().insertAll(mangas)
                        }

                        hasMoreData = mangas.isNotEmpty()
                        hasNetworkDataLoaded = mangas.isNotEmpty()
                        return MediatorResult.Success(endOfPaginationReached = !hasMoreData)
                    }
                    is Resource.Loading -> {
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                }
            } else {
                return MediatorResult.Success(endOfPaginationReached = !hasMoreData)
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}
