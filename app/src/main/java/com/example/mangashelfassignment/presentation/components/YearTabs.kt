package com.example.mangashelfassignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mangashelfassignment.ui.theme.Colors.blueBlack
import com.example.mangashelfassignment.ui.theme.Colors.ltGrey
import com.example.mangashelfassignment.ui.theme.Colors.white1


@Composable
fun YearTabs(
    years: List<Int>,
    selectedYear: Int?,
    onYearSelected: (Int) -> Unit,
    backgroundColor: Color
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(selectedYear) {
        val index = years.indexOf(selectedYear)
        if (index != -1) {
            val targetScroll = index * 100
            scrollState.animateScrollTo(targetScroll)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .horizontalScroll(scrollState)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        years.forEach { year ->
            val isSelected = year == selectedYear
            val tabColor = if (isSelected) blueBlack else ltGrey
            val textColor = if (isSelected) white1 else blueBlack

            Box(
                modifier = Modifier
                    .background(tabColor, shape = RoundedCornerShape(8.dp))
                    .clickable { onYearSelected(year) }
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(text = year.toString(), color = textColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}
