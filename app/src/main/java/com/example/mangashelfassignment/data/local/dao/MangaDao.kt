package com.example.mangashelfassignment.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mangashelfassignment.data.local.entity.MangaEntity
import com.example.mangashelfassignment.data.local.entity.MangaFlaggedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Query("SELECT * FROM mangaentity ORDER BY publishedDate ASC")
    fun getPagedMangaByYearAsc(): PagingSource<Int, MangaEntity>

    @Query("SELECT * FROM mangaentity ORDER BY score ASC")
    fun getPagedMangaByScoreAsc(): PagingSource<Int, MangaEntity>

    @Query("SELECT * FROM mangaentity ORDER BY score DESC")
    fun getPagedMangaByScoreDesc(): PagingSource<Int, MangaEntity>

    @Query("SELECT * FROM mangaentity ORDER BY popularity ASC")
    fun getPagedMangaByPopularityAsc(): PagingSource<Int, MangaEntity>

    @Query("SELECT * FROM mangaentity ORDER BY popularity DESC")
    fun getPagedMangaByPopularityDesc(): PagingSource<Int, MangaEntity>


    @Query("SELECT COUNT(*) FROM mangaentity")
    suspend fun getMangaCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(actionlogs: List<MangaEntity>)

    @Query("SELECT id, isFavorite, isRead FROM mangaentity")
    suspend fun getAllMangaWithFlags(): List<MangaFlaggedEntity>

    @Query("DELETE FROM mangaentity")
    suspend fun clearAll()

    @Query("UPDATE mangaentity SET isFavorite=:isFavorite WHERE id=:id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("UPDATE mangaentity SET isRead=:isRead WHERE id=:id")
    suspend fun updateIsReadStatus(id: String, isRead: Boolean)

    @Query("SELECT DISTINCT CAST(strftime('%Y', publishedDate, 'unixepoch') AS INTEGER) FROM mangaentity ORDER BY publishedDate ASC")
    suspend fun getDistinctPublicationYears(): List<Int>

    @Query("SELECT id FROM mangaentity WHERE strftime('%Y', publishedDate, 'unixepoch') = :year ORDER BY publishedDate ASC LIMIT 1")
    suspend fun getFirstMangaIdForYear(year: String): String?

    @Query("SELECT * FROM mangaentity WHERE id=:id")
    fun getMangaById(id: String): Flow<MangaEntity?>

    @Query("SELECT * FROM mangaentity WHERE isRead=0 and category = :category ORDER BY popularity DESC, score DESC")
    fun fetchRecommendedManga(category: String): PagingSource<Int, MangaEntity>

}