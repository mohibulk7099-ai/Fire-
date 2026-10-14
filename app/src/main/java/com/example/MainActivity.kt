package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.AppBottomNav
import com.example.ui.components.GenerationProgressDialog
import com.example.ui.components.TopHeader
import com.example.ui.components.VideoDetailDialog
import com.example.ui.screens.CreationsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PhotoToVideoScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TextToVideoScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isHindi = viewModel.isHindi()
    val credits by viewModel.creditsFlow.collectAsState()
    val canClaimDaily by viewModel.canClaimDaily.collectAsState()
    val dailyRemainingMs by viewModel.dailyRemainingMs.collectAsState()
    val recentVideos by viewModel.recentVideos.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState()
    val activeJob by viewModel.activeGeneratingJob.collectAsState()
    val selectedVideoForPlayer by viewModel.selectedVideoForPlayer.collectAsState()
    val toastMsg by viewModel.toastMessage.collectAsState()

    // Handle toast messages
    LaunchedEffect(toastMsg) {
        toastMsg?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Predictive back handling
    BackHandler(enabled = selectedVideoForPlayer != null || currentScreen != Screen.HOME) {
        if (selectedVideoForPlayer != null) {
            viewModel.closePlayer()
        } else if (currentScreen != Screen.HOME) {
            viewModel.navigateTo(Screen.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            TopHeader(
                isHindi = isHindi,
                credits = credits,
                userName = viewModel.preferences.getUserName(),
                onLanguageToggle = {
                    viewModel.switchLanguage(if (isHindi) "en" else "hi")
                },
                onProfileClick = {
                    viewModel.navigateTo(Screen.SETTINGS)
                }
            )
        },
        bottomBar = {
            AppBottomNav(
                currentScreen = currentScreen,
                isHindi = isHindi,
                onNavigate = { screen ->
                    viewModel.navigateTo(screen)
                }
            )
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        isHindi = isHindi,
                        credits = credits,
                        canClaimDaily = canClaimDaily,
                        dailyRemainingMs = dailyRemainingMs,
                        recentVideos = recentVideos,
                        isDemoMode = viewModel.preferences.isDemoMode(),
                        onNavigate = { screen -> viewModel.navigateTo(screen) },
                        onClaimDaily = { viewModel.claimDailyBonus() },
                        onVideoClick = { video -> viewModel.playVideo(video) }
                    )
                }
                Screen.TEXT_TO_VIDEO -> {
                    TextToVideoScreen(
                        isHindi = isHindi,
                        userCredits = credits,
                        onGenerate = { prompt, style, aspect, duration, quality ->
                            viewModel.startTextToVideo(prompt, style, aspect, duration, quality)
                        },
                        onShowToast = { msg -> viewModel.showToast(msg) }
                    )
                }
                Screen.PHOTO_TO_VIDEO -> {
                    PhotoToVideoScreen(
                        isHindi = isHindi,
                        userCredits = credits,
                        onGenerate = { photoUri, motionPrompt, style, aspect, duration, quality ->
                            viewModel.startPhotoToVideo(photoUri, motionPrompt, style, aspect, duration, quality)
                        },
                        onShowToast = { msg -> viewModel.showToast(msg) }
                    )
                }
                Screen.CREATIONS -> {
                    CreationsScreen(
                        isHindi = isHindi,
                        videos = allVideos,
                        onVideoClick = { video -> viewModel.playVideo(video) },
                        onDeleteVideo = { video -> viewModel.deleteVideo(video) },
                        onRetryVideo = { video -> viewModel.retryJob(video) },
                        onShowToast = { msg -> viewModel.showToast(msg) }
                    )
                }
                Screen.SETTINGS -> {
                    SettingsScreen(
                        isHindi = isHindi,
                        preferences = viewModel.preferences,
                        onLanguageChange = { lang -> viewModel.switchLanguage(lang) },
                        onSaveSettings = { demoMode, apiKey, backendUrl ->
                            viewModel.saveProviderSettings(demoMode, apiKey, backendUrl)
                        },
                        onResetData = { viewModel.resetAllData() },
                        onShowToast = { msg -> viewModel.showToast(msg) }
                    )
                }
            }
        }
    }

    // Active generation progress modal
    activeJob?.let { job ->
        GenerationProgressDialog(
            job = job,
            isHindi = isHindi,
            onCancel = { viewModel.cancelActiveJob() },
            onWatch = { completedVideo ->
                viewModel.dismissActiveJobModal()
                viewModel.playVideo(completedVideo)
            },
            onDismiss = {
                viewModel.dismissActiveJobModal()
            }
        )
    }

    // Video Player & Detail modal
    selectedVideoForPlayer?.let { video ->
        VideoDetailDialog(
            video = video,
            isHindi = isHindi,
            onDismiss = { viewModel.closePlayer() },
            onDelete = { videoToDelete ->
                viewModel.deleteVideo(videoToDelete)
            }
        )
    }
}
