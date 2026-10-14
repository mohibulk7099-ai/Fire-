package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppPreferences
import com.example.data.VideoEntity
import com.example.model.AspectRatio
import com.example.model.VideoStyle
import com.example.service.VideoGenerationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    TEXT_TO_VIDEO,
    PHOTO_TO_VIDEO,
    CREATIONS,
    SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val database = AppDatabase.getDatabase(application)
    val preferences = AppPreferences(application)
    val generationService = VideoGenerationService(application, database, preferences)

    val languageFlow: StateFlow<String> = preferences.languageFlow
    val creditsFlow: StateFlow<Int> = preferences.creditsFlow

    private val _canClaimDaily = MutableStateFlow(preferences.canClaimDailyBonus())
    val canClaimDaily: StateFlow<Boolean> = _canClaimDaily.asStateFlow()

    private val _dailyRemainingMs = MutableStateFlow(preferences.getRemainingCooldownMs())
    val dailyRemainingMs: StateFlow<Long> = _dailyRemainingMs.asStateFlow()

    val allVideos: StateFlow<List<VideoEntity>> = database.videoDao().getAllVideos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentVideos: StateFlow<List<VideoEntity>> = database.videoDao().getRecentCompletedVideos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeGeneratingJobId = MutableStateFlow<String?>(null)
    val activeGeneratingJobId: StateFlow<String?> = _activeGeneratingJobId.asStateFlow()

    private val _activeGeneratingJob = MutableStateFlow<VideoEntity?>(null)
    val activeGeneratingJob: StateFlow<VideoEntity?> = _activeGeneratingJob.asStateFlow()

    private val _selectedVideoForPlayer = MutableStateFlow<VideoEntity?>(null)
    val selectedVideoForPlayer: StateFlow<VideoEntity?> = _selectedVideoForPlayer.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        // Ticker for daily claim remaining time
        viewModelScope.launch {
            while (isActive) {
                _canClaimDaily.value = preferences.canClaimDailyBonus()
                _dailyRemainingMs.value = preferences.getRemainingCooldownMs()
                delay(1000)
            }
        }

        // Observe active job if running
        viewModelScope.launch {
            allVideos.collect { list ->
                val activeId = _activeGeneratingJobId.value
                if (activeId != null) {
                    val matching = list.find { it.id == activeId }
                    _activeGeneratingJob.value = matching
                    if (matching?.status == "COMPLETED" || matching?.status == "FAILED") {
                        // After delay, clear active modal so user sees completion
                    }
                }
            }
        }
    }

    fun isHindi(): Boolean = languageFlow.value == "hi"

    fun switchLanguage(lang: String) {
        preferences.setLanguage(lang)
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun claimDailyBonus(): Boolean {
        val success = preferences.claimDailyBonus()
        if (success) {
            _canClaimDaily.value = false
            _dailyRemainingMs.value = AppPreferences.DAILY_COOLDOWN_MS
            showToast(if (isHindi()) "+20 फ्री क्रेडिट आपके खाते में जुड़ गए!" else "+20 Free Credits added to your account!")
        }
        return success
    }

    fun startTextToVideo(
        prompt: String,
        style: VideoStyle,
        aspectRatio: AspectRatio,
        durationSeconds: Int,
        quality: String
    ) {
        generationService.startTextToVideoJob(
            prompt = prompt,
            style = style,
            aspectRatio = aspectRatio,
            durationSeconds = durationSeconds,
            quality = quality,
            isHindi = isHindi(),
            onJobStarted = { jobId ->
                _activeGeneratingJobId.value = jobId
            },
            onError = { err ->
                showToast(err)
            }
        )
    }

    fun startPhotoToVideo(
        photoUri: String,
        motionPrompt: String?,
        style: VideoStyle,
        aspectRatio: AspectRatio,
        durationSeconds: Int,
        quality: String
    ) {
        generationService.startPhotoToVideoJob(
            photoUri = photoUri,
            motionPrompt = motionPrompt,
            style = style,
            aspectRatio = aspectRatio,
            durationSeconds = durationSeconds,
            quality = quality,
            isHindi = isHindi(),
            onJobStarted = { jobId ->
                _activeGeneratingJobId.value = jobId
            },
            onError = { err ->
                showToast(err)
            }
        )
    }

    fun cancelActiveJob() {
        val jobId = _activeGeneratingJobId.value
        if (jobId != null) {
            generationService.cancelJob(jobId)
            showToast(if (isHindi()) "जनरेशन रद्द कर दी गई। 10 क्रेडिट वापस किए गए।" else "Generation cancelled. 10 credits refunded.")
            _activeGeneratingJobId.value = null
            _activeGeneratingJob.value = null
        }
    }

    fun dismissActiveJobModal() {
        _activeGeneratingJobId.value = null
        _activeGeneratingJob.value = null
    }

    fun retryJob(video: VideoEntity) {
        generationService.retryJob(
            video = video,
            isHindi = isHindi(),
            onStarted = { newJobId ->
                _activeGeneratingJobId.value = newJobId
            },
            onError = { err ->
                showToast(err)
            }
        )
    }

    fun deleteVideo(video: VideoEntity) {
        viewModelScope.launch {
            database.videoDao().deleteVideo(video)
            showToast(if (isHindi()) "वीडियो हटा दिया गया" else "Video deleted")
        }
    }

    fun playVideo(video: VideoEntity) {
        _selectedVideoForPlayer.value = video
    }

    fun closePlayer() {
        _selectedVideoForPlayer.value = null
    }

    fun saveProviderSettings(demoMode: Boolean, apiKey: String, backendUrl: String) {
        preferences.setDemoMode(demoMode)
        preferences.setCustomApiKey(apiKey)
        preferences.setCustomBackendUrl(backendUrl)
        showToast(if (isHindi()) "सेटिंग्स सुरक्षित रूप से सहेज ली गईं!" else "Settings saved successfully!")
    }

    fun resetAllData() {
        viewModelScope.launch {
            preferences.resetAllData()
            database.videoDao().clearAll()
            _activeGeneratingJobId.value = null
            _activeGeneratingJob.value = null
            _selectedVideoForPlayer.value = null
            showToast(if (isHindi()) "डेटा रीसेट हो गया (50 क्रेडिट रीस्टोर)" else "Data reset (50 credits restored)")
        }
    }
}
