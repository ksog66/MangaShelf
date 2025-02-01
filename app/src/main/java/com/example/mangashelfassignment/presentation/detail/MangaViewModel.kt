package com.example.mangashelfassignment.presentation.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangashelfassignment.data.mapper.toManga
import com.example.mangashelfassignment.data.repo.MangaRepository
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.presentation.navigation.MANGA_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mangaId: String = savedStateHandle[MANGA_ID_KEY] ?: ""

    private val _uiState = MutableStateFlow<MangaDetailUiState>(MangaDetailUiState.Loading)
    val uiState: StateFlow<MangaDetailUiState> = _uiState


    init {
        fetchManga()
    }

    fun fetchManga() {
        viewModelScope.launch {
            _uiState.value = MangaDetailUiState.Loading
            try {
                if (mangaId.isEmpty()) {
                    throw Exception("Invalid Manga Id")
                }

                mangaRepository.fetchManga(mangaId).collectLatest { mangaEntity ->
                    if (mangaEntity != null) {
                        _uiState.value = MangaDetailUiState.Success(mangaEntity.toManga())
                    } else {
                        _uiState.value = MangaDetailUiState.Error("Couldn't find the manga")
                    }
                }

            } catch (e: Exception) {
                Log.e("MangaViewModel",  """
                    Error while fetching manga data ${e.message}
                """.trimIndent(), )
                _uiState.value = MangaDetailUiState.Error(e.message)
            }
        }
    }

    fun updateFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            mangaRepository.updateFavoriteStatus(id, isFavorite)
        }
    }

    fun markAsRead(id: String, isRead: Boolean) {
        viewModelScope.launch {
            mangaRepository.markAsRead(id, isRead)
        }
    }


}
sealed interface MangaDetailUiState {
    data object Loading: MangaDetailUiState

    data class Error(val message: String?): MangaDetailUiState

    data class Success(val manga:Manga): MangaDetailUiState

}