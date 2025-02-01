package com.example.mangashelfassignment.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.mangashelfassignment.data.mapper.toManga
import com.example.mangashelfassignment.data.repo.MangaRepository
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.presentation.components.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mangaRepository: MangaRepository
) : ViewModel() {

    private val _isInitialLoading = mutableStateOf(true)
    val isInitialLoading: State<Boolean> = _isInitialLoading

    var mangaPager: Flow<PagingData<Manga>>

    var publishedYears = mutableStateListOf<Int>()

    var selectedYear = mutableStateOf<Int?>(null)
        private set

    var firstMangaIdForSelectedYear = mutableStateOf("")
        private set

    var selectedSortOption = mutableStateOf<SortOption?>(null)
        private set

    init {
        try {
            mangaPager = mangaRepository.getPagedManga(selectedSortOption.value)
                .map { pagingData ->
                    _isInitialLoading.value = false
                    pagingData.map { mangaEntity ->
                        mangaEntity.toManga()
                    }
                }.cachedIn(viewModelScope)
        } catch (exception: Exception) {
            mangaPager = flow { PagingData.empty<Manga>() }
        }
    }

    fun resetScrolling() {
        firstMangaIdForSelectedYear.value = ""
    }

    fun yearSelected(year: Int) {
        getFirstMangaIdForYear(year.toString())
    }

    fun yearScrolled(year: Int) {
        selectedYear.value = year
    }

    fun sortOptionChanged(sortOption: SortOption?) {
        if (sortOption == selectedSortOption.value) {
            return
        }
        selectedSortOption.value = sortOption
        if (sortOption == null) {
            selectedYear.value = publishedYears.firstOrNull()
        }
        mangaPager = try {
            mangaRepository.getPagedManga(sortOption)
                .map { pagingData ->
                    pagingData.map { mangaEntity ->
                        mangaEntity.toManga()
                    }
                }.cachedIn(viewModelScope)
        } catch (exception: Exception) {
            flow { PagingData.empty<Manga>() }
        }
    }

    fun updateFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            mangaRepository.updateFavoriteStatus(id, isFavorite)
        }
    }

    fun fetchDistinctPublicationYear() {
        viewModelScope.launch {
            publishedYears.clear()
            publishedYears.addAll(mangaRepository.fetchDistinctPublicationYears())
            selectedYear.value = publishedYears.firstOrNull()
        }
    }

    private fun getFirstMangaIdForYear(year: String) {
        viewModelScope.launch {
            val id = mangaRepository.getFirstMangaIdForYear(year)
            if (id != null) {
                firstMangaIdForSelectedYear.value = id
                selectedYear.value = year.toInt()
            }
        }
    }
}