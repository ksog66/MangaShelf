package com.example.mangashelfassignment.presentation.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mangashelfassignment.R
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.presentation.components.GenericErrorScreen
import com.example.mangashelfassignment.presentation.components.HorizontalLoadingItem
import com.example.mangashelfassignment.presentation.components.HorizontalMangaCard
import com.example.mangashelfassignment.presentation.components.LoadingScreen
import com.example.mangashelfassignment.ui.theme.Colors.white1
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MangaDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: MangaViewModel = hiltViewModel(),
    navigateToMangaDetail: (String) -> Unit,
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recommendedMangas = viewModel.recommendedMangaPager.collectAsLazyPagingItems()
    when (uiState) {
        is MangaDetailUiState.Error -> {
            val message = (uiState as MangaDetailUiState.Error).message
            GenericErrorScreen(
                modifier = modifier,
                message = message,
                ctaText = stringResource(R.string.retry),
                onCtaClick = viewModel::fetchManga
            )
        }

        is MangaDetailUiState.Loading -> {
            LoadingScreen(modifier, stringResource(R.string.loading_item_for_you))
        }

        is MangaDetailUiState.Success -> {
            val data = (uiState as MangaDetailUiState.Success)
            MangaDetailScreen(
                modifier = modifier,
                manga = data.manga,
                recommendedMangas = recommendedMangas,
                updateFavorite = viewModel::updateFavorite,
                markAsRead = viewModel::markAsRead,
                navigateToMangaDetail = navigateToMangaDetail,
                onBackClick = navigateBack
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(
    modifier: Modifier = Modifier,
    manga: Manga,
    recommendedMangas: LazyPagingItems<Manga>,
    updateFavorite: (String, Boolean) -> Unit,
    markAsRead: (String, Boolean) -> Unit,
    navigateToMangaDetail: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(manga.title, fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        val publishedDate =
            SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(manga.publishedDate)
        val lazyListState = rememberLazyListState()
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(manga.image)
                    .build(),
                contentDescription = "Poster for manga ${manga.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(300.dp)
                    .clip(RoundedCornerShape(44.dp))
                    .background(white1),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.category_type, manga.category),
                fontSize = 16.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.score_colon_n, manga.score),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.popularity_colon_n, manga.popularity),
                fontSize = 18.sp, fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.published_on, publishedDate),
                fontSize = 16.sp, fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        updateFavorite(manga.id, !manga.isFavorite)
                    }
                ) {
                    Icon(
                        imageVector = if (manga.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.favorite),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = {
                        markAsRead(manga.id, !manga.isRead)
                    }
                ) {
                    Icon(
                        imageVector = if (manga.isRead) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = "Read"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.mark_as_read),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (recommendedMangas.loadState.refresh !is LoadState.Error && recommendedMangas.itemSnapshotList.isNotEmpty() && manga.isRead) {
                Text(
                    text = stringResource(R.string.you_might_like),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    state = lazyListState
                ) {
                    items(
                        count = recommendedMangas.itemCount,
                        key = recommendedMangas.itemKey { it.id },
                        contentType = recommendedMangas.itemContentType {
                            "Recommended Mangas"
                        }
                    ) { index ->
                        val recommendedManga = recommendedMangas[index]

                        Log.d("RecommendedManga", """
                        Item Count -> ${recommendedMangas.itemCount}
                    """.trimIndent())
                        if (recommendedManga != null) {
                            HorizontalMangaCard(
                                manga = recommendedManga,
                                onCardClick = navigateToMangaDetail
                            )
                        }

                        if (recommendedMangas.loadState.append == LoadState.Loading) {
                            HorizontalLoadingItem()
                        }
                    }
                }
            }
        }
    }
}
