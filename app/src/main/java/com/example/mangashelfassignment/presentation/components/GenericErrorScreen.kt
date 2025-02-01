package com.example.mangashelfassignment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangashelfassignment.R
import com.example.mangashelfassignment.ui.theme.Colors.blueBlack
import com.example.mangashelfassignment.ui.theme.Colors.white1
import com.example.mangashelfassignment.ui.theme.MangaShelfAssignmentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericErrorScreen(
    modifier: Modifier = Modifier,
    message: String?,
    ctaText: String,
    onCtaClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = white1),

        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 32.dp),
                painter = painterResource(R.drawable.idle_illustration),
                contentDescription = "Error Screen"
            )

            Text(
                modifier = Modifier.padding(top = 32.dp, start = 24.dp, end = 24.dp),
                text = message ?: stringResource(R.string.generic_error_message),
                style = TextStyle.Default.copy(fontSize = 22.sp),
                maxLines = 2,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            Column() {

                Button(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = onCtaClick,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blueBlack
                    )
                ) {
                    Text(
                        text = ctaText,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = white1)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun GenericErrorScreenPreview() {
    MangaShelfAssignmentTheme {
        GenericErrorScreen(
            message = "Please Check your internet connection and try again",
            ctaText = "Retry",
            onCtaClick = {}
        )
    }
}