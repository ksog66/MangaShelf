package com.example.mangashelfassignment.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.mangashelfassignment.R
import com.example.mangashelfassignment.presentation.Manga
import com.example.mangashelfassignment.presentation.components.GenericErrorScreen
import com.example.mangashelfassignment.presentation.components.LoadingScreen
import com.example.mangashelfassignment.presentation.components.MangaCard
import com.example.mangashelfassignment.presentation.components.MsBottomSheet
import com.example.mangashelfassignment.presentation.components.SortOption
import com.example.mangashelfassignment.presentation.components.VerticalLoadingItem
import com.example.mangashelfassignment.presentation.components.YearTabs
import com.example.mangashelfassignment.ui.theme.Colors.dangerRed
import com.example.mangashelfassignment.ui.theme.Colors.white1
import com.example.mangashelfassignment.ui.theme.MangaShelfAssignmentTheme
import com.example.mangashelfassignment.util.disableUIInteraction
import com.example.mangashelfassignment.util.enableUIInteraction
import com.example.mangashelfassignment.util.getActivityOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val sortOption by viewModel.selectedSortOption
    val years = viewModel.publishedYears
    val selectedYear by viewModel.selectedYear

    val firstMangaIdForSelectedYear by viewModel.firstMangaIdForSelectedYear

    val isInitialLoading by viewModel.isInitialLoading
    val mangaLazyList = viewModel.mangaPager.collectAsLazyPagingItems()

    LaunchedEffect(years, mangaLazyList.itemCount) {
        if (years.isEmpty()) {
            viewModel.fetchDistinctPublicationYear()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(white1)) {
        if (isInitialLoading) {
            LoadingScreen(
                modifier = modifier
                    .fillMaxSize()
                    .background(white1),
                stringResource(R.string.loading_mangas_for_you)
            )
        } else if (mangaLazyList.itemSnapshotList.isEmpty() && mangaLazyList.loadState.refresh is LoadState.Error){
            val e = mangaLazyList.loadState.refresh as LoadState.Error
            val message = e.error.message
            GenericErrorScreen(
                modifier = Modifier,
                message = message,
                ctaText = stringResource(R.string.retry),
                onCtaClick = {
                    mangaLazyList.refresh()
                }
            )
        } else {
            HomeScreen(
                modifier = modifier,
                years = years,
                selectedSortOption = sortOption,
                selectedYear = selectedYear,
                scrollToMangaId = firstMangaIdForSelectedYear,
                resetScrolling = viewModel::resetScrolling,
                onYearSelected = viewModel::yearSelected,
                onYearScrolled = viewModel::yearScrolled,
                sortOptionChanged = viewModel::sortOptionChanged,
                mangaList = mangaLazyList,
                updateFavorite = viewModel::updateFavorite,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    years: List<Int>,
    selectedYear: Int?,
    selectedSortOption: SortOption?,
    scrollToMangaId: String,
    mangaList: LazyPagingItems<Manga>,
    resetScrolling: () -> Unit,
    onYearSelected: (Int) -> Unit,
    onYearScrolled: (Int) -> Unit,
    sortOptionChanged: (SortOption?) -> Unit,
    updateFavorite: (String, Boolean) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context.getActivityOrNull()
    val lazyListState = rememberLazyListState()

    val firstVisibleItemIndex = remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

    var showSortBottomSheet by remember { mutableStateOf(false) }

    val sheetState: SheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(firstVisibleItemIndex.value) {
        if (mangaList.itemSnapshotList.isNotEmpty() && scrollToMangaId.isEmpty()) {
            val firstVisibleManga = mangaList.itemSnapshotList[firstVisibleItemIndex.value]
            if (firstVisibleManga != null) {
                val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                val publishedYear = yearFormat.format(firstVisibleManga.publishedDate).toInt()
                if (selectedYear != publishedYear) {
                    onYearScrolled(publishedYear)
                }
            }
        }
    }

    LaunchedEffect(scrollToMangaId, mangaList.itemSnapshotList.items.size) {
        if (scrollToMangaId.isNotEmpty()) {
            val mangaToScroll = mangaList.itemSnapshotList.items.find { it.id == scrollToMangaId }
            if (mangaToScroll != null) {
                val scrollIndex = mangaList.itemSnapshotList.items.indexOf(mangaToScroll)
                lazyListState.scrollToItem(scrollIndex, scrollOffset = 0)
                resetScrolling.invoke()
                activity?.enableUIInteraction()
            } else {
                val lastIndex = mangaList.itemSnapshotList.items.size - 1
                if (lastIndex >= 0) {
                    lazyListState.scrollToItem(lastIndex, scrollOffset = 0)
                    activity?.disableUIInteraction()
                }
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = white1),
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    Row(
                        modifier = Modifier.clickable {
                            showSortBottomSheet = true
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.SwapVert, contentDescription = stringResource(R.string.sort_option))
                        Text(text = stringResource(R.string.sort_by))
                    }
                }
            )
        }
    ) { paddingValues ->

        MsBottomSheet(
            onDismissRequest = {
                showSortBottomSheet = false
            },
            showSheet = showSortBottomSheet,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sort_by),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                SortOption.entries.forEach { option ->
                    SortItem(
                        sort = option,
                        selected = selectedSortOption == option,
                        onSelected = {
                            scope.launch {
                                lazyListState.scrollToItem(0)
                            }
                            sortOptionChanged(option)
                            showSortBottomSheet = false
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        Column(modifier = Modifier.padding(paddingValues)) {
            if (selectedSortOption == null) {
                YearTabs(
                    years = years,
                    selectedYear = selectedYear,
                    onYearSelected = onYearSelected,
                    backgroundColor = white1
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.sort_by_colon), fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = selectedSortOption.sName, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.reset),
                        modifier = Modifier
                            .clickable {
                                sortOptionChanged(null)
                                showSortBottomSheet = false
                            }
                            .padding(12.dp),
                        color = dangerRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(white1),
                state = lazyListState
            ) {
                items(
                    count = mangaList.itemCount,
                    key = mangaList.itemKey { manga -> manga.id },
                    contentType = mangaList.itemContentType {
                        "Manga"
                    }
                ) { index: Int ->
                    val feedItem = mangaList[index]

                    if (feedItem != null) {
                        MangaCard(
                            manga = feedItem,
                            onFavoriteClick = updateFavorite,
                            onCardClick = navigateToDetail
                        )
                    }
                }
                item {
                    if (mangaList.loadState.refresh is LoadState.Error) {
                        val e = mangaList.loadState.refresh as LoadState.Error
                        val message = e.error.message
                        ErrorLog(text = message)
                    }
                }
            }
            if (mangaList.loadState.append == LoadState.Loading) {
                VerticalLoadingItem()
            }
        }
    }
}

@Composable
fun SortItem(
    modifier: Modifier = Modifier,
    sort: SortOption,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelected)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = sort.sName)
    }
}

@Composable
fun ErrorLog(
    modifier: Modifier = Modifier,
    text: String?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(shape = RoundedCornerShape(8.dp), color = dangerRed)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = "Error fetching manga list"
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = text ?: stringResource(R.string.generic_error_message),
            style = TextStyle.Default.copy(fontSize = 14.sp, color = white1),
        )
    }

}

@Preview
@Composable
private fun HomeScreenPreview() {
    MangaShelfAssignmentTheme {
        HomeRoute {

        }
    }
}

