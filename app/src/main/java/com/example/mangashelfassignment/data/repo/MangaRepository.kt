package com.example.mangashelfassignment.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.mangashelfassignment.data.local.MangaDb
import com.example.mangashelfassignment.data.local.entity.MangaEntity
import com.example.mangashelfassignment.domain.DataSource
import com.example.mangashelfassignment.domain.MangaRemoteMediator
import com.example.mangashelfassignment.presentation.components.SortOption
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaRepository @Inject constructor(
    private val dataSource: DataSource,
    private val db: MangaDb
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPagedManga(sortOption: SortOption?): Flow<PagingData<MangaEntity>> {
        val pagingSourceFactory: () -> PagingSource<Int, MangaEntity> = {
            when (sortOption) {
                SortOption.SCORE_ASC -> db.mangaDao().getPagedMangaByScoreAsc()
                SortOption.SCORE_DESC -> db.mangaDao().getPagedMangaByScoreDesc()
                SortOption.POPULARITY_ASC -> db.mangaDao().getPagedMangaByPopularityAsc()
                SortOption.POPULARITY_DESC -> db.mangaDao().getPagedMangaByPopularityDesc()
                else -> db.mangaDao().getPagedMangaByYearAsc()
            }
        }
        return Pager(
            config = PagingConfig(
                initialLoadSize = 20,
                prefetchDistance = 5,
                pageSize = 20,
                enablePlaceholders = false,
                jumpThreshold = 20
            ),
            remoteMediator = MangaRemoteMediator(dataSource, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun fetchManga(id: String): Flow<MangaEntity?> {
        return db.mangaDao().getMangaById(id)
    }
    suspend fun markAsRead(id: String, isRead: Boolean) {
        db.mangaDao().updateIsReadStatus(id, isRead)
    }

    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        db.mangaDao().updateFavoriteStatus(id, isFavorite)
    }

    suspend fun fetchDistinctPublicationYears(): List<Int> {
        return db.mangaDao().getDistinctPublicationYears()
    }

    suspend fun getFirstMangaIdForYear(year: String): String? {
        return db.mangaDao().getFirstMangaIdForYear(year)
    }

}
