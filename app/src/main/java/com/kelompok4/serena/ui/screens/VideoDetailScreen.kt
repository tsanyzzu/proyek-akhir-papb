package com.kelompok4.serena.ui.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.kelompok4.serena.R

/**
 * Data class representing a self-care video.
 */
data class SampleVideo(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: Int,
    val videoRes: Int,
    val duration: String
)

/**
 * Static video data for the selfcare section.
 * Replace the videoRes with your actual video resource IDs when you add videos to res/raw folder.
 */
object VideoData {
    val videos = listOf(
        SampleVideo(
            id = 1,
            title = "Gerakan Yoga Sederhana untuk Awali Harimu",
            description = "Di sini, kamu akan diajak melalui serangkaian gerakan yoga yang dirancang khusus untuk memulai pagi dengan penuh energi dan kesegaran. Gerakan-gerakan ini sangat cocok untuk pemula dan dapat membantu meningkatkan fleksibilitas, mengurangi stres, dan meningkatkan konsentrasi.\n\nManfaat:\n• Meningkatkan fleksibilitas tubuh\n• Mengurangi kecemasan dan stres\n• Meningkatkan fokus dan konsentrasi\n• Memperbaiki postur tubuh\n• Memberikan energi positif untuk memulai hari",
            thumbnail = R.drawable.onboarding_1,
            videoRes = R.raw.sample_video,
            duration = "5:30"
        ),
        SampleVideo(
            id = 2,
            title = "Teknik Pernapasan untuk Mengurangi Stres",
            description = "Pelajari teknik pernapasan dalam yang efektif untuk menenangkan pikiran dan mengurangi tingkat stres. Video ini akan memandu kamu melalui berbagai teknik pernapasan yang dapat dipraktikkan kapan saja dan di mana saja.\n\nManfaat:\n• Menurunkan tingkat kecemasan\n• Membantu tidur lebih nyenyak\n• Meningkatkan fokus dan kejernihan pikiran\n• Menurunkan tekanan darah\n• Meningkatkan kesehatan paru-paru",
            thumbnail = R.drawable.onboarding_2,
            videoRes = R.raw.sample_video,
            duration = "8:15"
        ),
        SampleVideo(
            id = 3,
            title = "Meditasi Pagi untuk Pikiran Tenang",
            description = "Mulai harimu dengan meditasi yang menenangkan. Panduan meditasi ini cocok untuk pemula dan akan membantu kamu mengembangkan kebiasaan meditasi yang konsisten untuk kesehatan mental yang lebih baik.\n\nManfaat:\n• Meningkatkan kesadaran diri\n• Mengurangi gejala depresi\n• Meningkatkan kualitas tidur\n• Memperkuat kesehatan emosional\n• Meningkatkan kebahagiaan secara keseluruhan",
            thumbnail = R.drawable.onboarding_3,
            videoRes = R.raw.sample_video,
            duration = "10:00"
        )
    )

    fun getVideoById(id: Int): SampleVideo? = videos.find { it.id == id }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDetailScreen(
    navController: NavHostController,
    videoId: Int
) {
    val video = VideoData.getVideoById(videoId) ?: return
    val context = LocalContext.current
    
    // Create ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = "android.resource://${context.packageName}/${video.videoRes}"
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
        }
    }
    
    // Release player when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Video") },
                navigationIcon = {
                    IconButton(onClick = { 
                        exoPlayer.stop()
                        navController.navigateUp() 
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Video Player
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Video Title
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Durasi: ${video.duration}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description Header
                Text(
                    text = "Deskripsi",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Video Description
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
