package com.example.serena.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.serena.ui.screens.SampleActivity
import com.kelompok4.serena.R
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.ui.PlayerView

/**
 * Detail screen for a self‑care activity.  Displays a video player along
 * with the activity title, view count/date and a description.  The video
 * content is bundled in the ``res/raw`` directory and played locally
 * using ExoPlayer.  There is no network call or API fetch for this
 * content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    navController: NavHostController,
    activityId: Int
) {
    // In a real app you would load the activity by ID from a repository.
    val activity = SampleActivity(
        id = activityId,
        title = "Gerakan Yoga Sederhana untuk Awali Harimu",
        thumbnail = R.drawable.serena_logo,
        date = "January 24, 2025",
        views = 12000,
        likes = 0
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Kegiatan") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Initialize the ExoPlayer to play a video stored in res/raw
            val context = LocalContext.current
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    val uri = RawResourceDataSource.buildRawResourceUri(R.raw.selfcare_video)
                    val mediaItem = MediaItem.fromUri(uri)
                    setMediaItem(mediaItem)
                    prepare()
                    playWhenReady = false
                }
            }
            // Ensure the player is released when the composable leaves
            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }
            // Display the video using PlayerView inside an AndroidView
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = activity.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = "${activity.views / 1000}k penonton • ${activity.date}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = "Di sini, kamu akan diajak melalui serangkaian gerakan yoga yang dirancang khusus untuk memulai pagi dengan penuh energi dan kesegaran. Video ini menjelaskan setiap gerakan secara detail sehingga kamu dapat mengikuti dengan mudah dan mendapatkan manfaat maksimal.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}