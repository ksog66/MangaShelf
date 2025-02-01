package com.example.mangashelfassignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mangashelfassignment.R
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.ui.theme.Colors.blueBlack
import com.example.mangashelfassignment.ui.theme.Colors.white1
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MangaCard(
    manga: Manga,
    onFavoriteClick: (String, Boolean) -> Unit,
    onCardClick: (String) -> Unit
) {
    val context = LocalContext.current
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val publishedYear = yearFormat.format(manga.publishedDate)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick(manga.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = white1)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(manga.image)
                    .build(),
                contentDescription = "Poster image for manga ${manga.title}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(white1),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = manga.title, fontSize = 18.sp, color = blueBlack, fontWeight = FontWeight.Bold)
                Text(text = stringResource(R.string.score_colon_n, manga.score), fontSize = 14.sp, color = blueBlack, fontWeight = FontWeight.Medium)
                Text(text = stringResource(R.string.popularity_colon_n, manga.popularity), fontSize = 14.sp, color = blueBlack, fontWeight = FontWeight.Medium)
                Text(text = stringResource(R.string.year, publishedYear), fontSize = 14.sp, color = blueBlack, fontWeight = FontWeight.Medium)
            }
            IconButton(onClick = { onFavoriteClick(manga.id, !manga.isFavorite) }) {
                Icon(
                    imageVector = if (manga.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}
