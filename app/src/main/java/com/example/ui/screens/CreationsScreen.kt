package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VideoEntity
import com.example.service.VideoDownloader
import com.example.ui.common.Strings
import com.example.ui.components.BadgeChip
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreationsScreen(
    isHindi: Boolean,
    videos: List<VideoEntity>,
    onVideoClick: (VideoEntity) -> Unit,
    onDeleteVideo: (VideoEntity) -> Unit,
    onRetryVideo: (VideoEntity) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        Strings.get("filter_all", isHindi),
        Strings.get("filter_completed", isHindi),
        Strings.get("filter_processing", isHindi),
        Strings.get("filter_failed", isHindi)
    )

    val filteredVideos = when (selectedTabIndex) {
        1 -> videos.filter { it.status == "COMPLETED" }
        2 -> videos.filter { it.status == "PROCESSING" || it.status == "QUEUED" }
        3 -> videos.filter { it.status == "FAILED" }
        else -> videos
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
    ) {
        // Title
        Text(
            text = Strings.get("creations_title", isHindi),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // Filter Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = NeonPurple,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = NeonPurple
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) NeonCyan else TextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = Strings.get("no_creations_msg", isHindi),
                        color = TextSecondary,
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredVideos, key = { it.id }) { video ->
                    CreationVideoCard(
                        video = video,
                        isHindi = isHindi,
                        onPlay = { onVideoClick(video) },
                        onDownload = {
                            video.videoUrl?.let { url ->
                                val fileName = "AIVideo_${video.id}.mp4"
                                VideoDownloader.downloadVideoWithDownloadManager(
                                    context = context,
                                    videoUrl = url,
                                    fileName = fileName,
                                    onSuccess = { onShowToast(Strings.get("download_complete", isHindi)) },
                                    onError = { onShowToast(it) }
                                )
                            }
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "AI Video: ${video.prompt}\n${video.videoUrl ?: ""}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, Strings.get("share_video", isHindi)))
                        },
                        onDelete = { onDeleteVideo(video) },
                        onRetry = { onRetryVideo(video) }
                    )
                }
            }
        }
    }
}

@Composable
fun CreationVideoCard(
    video: VideoEntity,
    isHindi: Boolean,
    onPlay: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .testTag("creation_item_${video.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Thumbnail
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .clickable(enabled = video.status == "COMPLETED") { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sample_cyberpunk_thumb),
                        contentDescription = video.prompt,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )

                    when (video.status) {
                        "COMPLETED" -> {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(NeonPurple.copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        "PROCESSING", "QUEUED" -> {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = "Rendering",
                                tint = NeonCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Failed",
                                tint = ErrorRed,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Information
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.prompt,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        BadgeChip(text = video.style.uppercase(), color = NeonPurple)
                        BadgeChip(text = video.aspectRatio, color = NeonCyan)
                        BadgeChip(text = "${video.durationSeconds}s", color = GoldYellow)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(video.createdAt))
                    Text(
                        text = dateStr,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    // Status pill
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (video.status) {
                            "COMPLETED" -> "✓ " + (if (isHindi) "सफलतापूर्वक पूर्ण" else "Completed")
                            "PROCESSING" -> "⏳ " + (if (isHindi) "रेंडरिंग जारी..." else "Rendering...")
                            else -> "✕ " + (if (isHindi) "असफल (रिफंड)" else "Failed (Refunded)")
                        },
                        color = when (video.status) {
                            "COMPLETED" -> SuccessGreen
                            "PROCESSING" -> NeonCyan
                            else -> ErrorRed
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (video.status == "FAILED") {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = NeonPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = Strings.get("retry_btn", isHindi),
                            color = NeonPurple,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (video.status == "COMPLETED") {
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Watch",
                            tint = NeonPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onDownload,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ErrorRed.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
