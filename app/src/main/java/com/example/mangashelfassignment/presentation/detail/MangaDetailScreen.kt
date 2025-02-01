package com.example.mangashelfassignment.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mangashelfassignment.R
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.presentation.components.GenericErrorScreen
import com.example.mangashelfassignment.presentation.components.LoadingScreen
import com.example.mangashelfassignment.ui.theme.Colors.white1
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MangaDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: MangaViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            val manga = (uiState as MangaDetailUiState.Success).manga
            MangaDetailScreen(
                modifier = modifier,
                manga = manga,
                updateFavorite = viewModel::updateFavorite,
                markAsRead = viewModel::markAsRead,
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
    updateFavorite: (String, Boolean) -> Unit,
    markAsRead: (String, Boolean) -> Unit,
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

        val publishedDate = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(manga.publishedDate)
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
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
            Text(text = stringResource(R.string.score_colon_n, manga.score), fontSize = 18.sp, fontWeight = FontWeight.Medium)
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
                    Text(stringResource(R.string.favorite), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    Text(stringResource(R.string.mark_as_read), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ManageDetailScreenPreview() {
    MangaDetailScreen(
        manga = Manga(
            id = "1", title = "Naruto", image = "", score = 9.0, popularity = 1,
            publishedDate = Date(), category = "Shonen", isFavorite = false, isRead = false
        ),
        updateFavorite = { _, _ -> },
        markAsRead = { _, _ -> },
        onBackClick = {}
    )
}
