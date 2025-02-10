package com.example.mangashelfassignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mangashelfassignment.R
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.ui.theme.Colors.blueBlack
import com.example.mangashelfassignment.ui.theme.Colors.white1
import com.example.mangashelfassignment.ui.theme.MangaShelfAssignmentTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HorizontalMangaCard(
    modifier: Modifier = Modifier,
    manga: Manga,
    onCardClick: (String) -> Unit
) {
    val context = LocalContext.current
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val publishedYear = yearFormat.format(manga.publishedDate)
    Card(
        modifier = modifier
            .wrapContentWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = 4.dp)
            .clickable { onCardClick(manga.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = white1)
    ) {

        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(manga.image)
                    .build(),
                contentDescription = "Poster image for manga ${manga.title}",
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(white1),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = manga.title,
                fontSize = 12.sp,
                color = blueBlack,
                overflow = TextOverflow.Clip,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.score_colon_n, manga.score),
                fontSize = 12.sp,
                color = blueBlack
            )
            Text(
                text = stringResource(R.string.popularity_colon_n, manga.popularity),
                fontSize = 12.sp,
                color = blueBlack
            )
            Text(
                text = stringResource(R.string.year, publishedYear),
                fontSize = 12.sp,
                color = blueBlack
            )
        }
    }
}

@Preview
@Composable
private fun HorizontalCardPreview() {
    MangaShelfAssignmentTheme {
        val manga = Manga(
            id = "4e70e91ac092255ef70016d6",
            image = "https://cdn.myanimelist.net/images/anime/6/73245.jpg",
            score = 16.5,
            popularity = 165588,
            title = "Neon Genesis Evangelion: Shinji Ikari Raising Project",
            publishedDate = Date(),
            category = "Mystery",
            isFavorite = false,
            isRead = false
        )
        HorizontalMangaCard(
            modifier = Modifier,
            manga = manga
        ) {

        }
    }
}