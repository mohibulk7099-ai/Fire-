package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ai_video_maker_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CREDITS = "user_credits"
        private const val KEY_LAST_DAILY_CLAIM = "last_daily_claim_time"
        private const val KEY_STREAK = "daily_streak_days"
        private const val KEY_TOTAL_GENERATIONS = "total_generations_count"
        private const val KEY_LANGUAGE = "app_language" // "hi" or "en"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_DEMO_MODE = "demo_mode_enabled"
        private const val KEY_CUSTOM_API_KEY = "custom_api_key"
        private const val KEY_CUSTOM_BACKEND_URL = "custom_backend_url"

        const val DEFAULT_STARTER_CREDITS = 50
        const val DAILY_BONUS_CREDITS = 20
        const val GENERATION_COST_CREDITS = 10
        const val DAILY_COOLDOWN_MS = 24 * 60 * 60 * 1000L // 24 hours
    }

    private val _creditsFlow = MutableStateFlow(getCredits())
    val creditsFlow: StateFlow<Int> = _creditsFlow.asStateFlow()

    private val _languageFlow = MutableStateFlow(getLanguage())
    val languageFlow: StateFlow<String> = _languageFlow.asStateFlow()

    init {
        // Initialize user ID and starter balance if first launch
        if (!prefs.contains(KEY_USER_ID)) {
            val randomId = UUID.randomUUID().toString().take(8)
            prefs.edit()
                .putString(KEY_USER_ID, "Guest_$randomId")
                .putString(KEY_USER_NAME, "क्रिएटर (Guest)")
                .putInt(KEY_CREDITS, DEFAULT_STARTER_CREDITS)
                .putString(KEY_LANGUAGE, "hi") // Hindi is default
                .putBoolean(KEY_DEMO_MODE, true)
                .apply()
            _creditsFlow.value = DEFAULT_STARTER_CREDITS
            _languageFlow.value = "hi"
        }
    }

    fun getUserId(): String = prefs.getString(KEY_USER_ID, "Guest_001") ?: "Guest_001"
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "क्रिएटर (Guest)") ?: "क्रिएटर (Guest)"
    fun setUserName(name: String) = prefs.edit().putString(KEY_USER_NAME, name).apply()

    fun getCredits(): Int = prefs.getInt(KEY_CREDITS, DEFAULT_STARTER_CREDITS)

    fun hasEnoughCredits(cost: Int = GENERATION_COST_CREDITS): Boolean = getCredits() >= cost

    @Synchronized
    fun deductCredits(amount: Int = GENERATION_COST_CREDITS): Boolean {
        val current = getCredits()
        if (current >= amount) {
            val newBalance = current - amount
            prefs.edit().putInt(KEY_CREDITS, newBalance).apply()
            _creditsFlow.value = newBalance
            incrementTotalGenerations()
            return true
        }
        return false
    }

    @Synchronized
    fun refundCredits(amount: Int = GENERATION_COST_CREDITS) {
        val current = getCredits()
        val newBalance = current + amount
        prefs.edit().putInt(KEY_CREDITS, newBalance).apply()
        _creditsFlow.value = newBalance
    }

    @Synchronized
    fun addCredits(amount: Int) {
        val current = getCredits()
        val newBalance = current + amount
        prefs.edit().putInt(KEY_CREDITS, newBalance).apply()
        _creditsFlow.value = newBalance
    }

    fun getLastDailyClaim(): Long = prefs.getLong(KEY_LAST_DAILY_CLAIM, 0L)

    fun canClaimDailyBonus(): Boolean {
        val lastClaim = getLastDailyClaim()
        val now = System.currentTimeMillis()
        return (now - lastClaim) >= DAILY_COOLDOWN_MS
    }

    fun getRemainingCooldownMs(): Long {
        val lastClaim = getLastDailyClaim()
        val elapsed = System.currentTimeMillis() - lastClaim
        return if (elapsed < DAILY_COOLDOWN_MS) DAILY_COOLDOWN_MS - elapsed else 0L
    }

    @Synchronized
    fun claimDailyBonus(): Boolean {
        if (canClaimDailyBonus()) {
            val streak = getStreakDays() + 1
            prefs.edit()
                .putLong(KEY_LAST_DAILY_CLAIM, System.currentTimeMillis())
                .putInt(KEY_STREAK, streak)
                .apply()
            addCredits(DAILY_BONUS_CREDITS)
            return true
        }
        return false
    }

    fun getStreakDays(): Int = prefs.getInt(KEY_STREAK, 0)

    fun getTotalGenerations(): Int = prefs.getInt(KEY_TOTAL_GENERATIONS, 0)
    private fun incrementTotalGenerations() {
        val count = getTotalGenerations() + 1
        prefs.edit().putInt(KEY_TOTAL_GENERATIONS, count).apply()
    }

    fun getLanguage(): String = prefs.getString(KEY_LANGUAGE, "hi") ?: "hi"
    fun setLanguage(lang: String) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply()
        _languageFlow.value = lang
    }

    fun isDemoMode(): Boolean = prefs.getBoolean(KEY_DEMO_MODE, true)
    fun setDemoMode(enabled: Boolean) = prefs.edit().putBoolean(KEY_DEMO_MODE, enabled).apply()

    fun getCustomApiKey(): String = prefs.getString(KEY_CUSTOM_API_KEY, "") ?: ""
    fun setCustomApiKey(key: String) = prefs.edit().putString(KEY_CUSTOM_API_KEY, key).apply()

    fun getCustomBackendUrl(): String = prefs.getString(KEY_CUSTOM_BACKEND_URL, "") ?: ""
    fun setCustomBackendUrl(url: String) = prefs.edit().putString(KEY_CUSTOM_BACKEND_URL, url).apply()

    fun resetAllData() {
        prefs.edit()
            .putInt(KEY_CREDITS, DEFAULT_STARTER_CREDITS)
            .putLong(KEY_LAST_DAILY_CLAIM, 0L)
            .putInt(KEY_STREAK, 0)
            .putInt(KEY_TOTAL_GENERATIONS, 0)
            .putString(KEY_LANGUAGE, "hi")
            .putBoolean(KEY_DEMO_MODE, true)
            .putString(KEY_CUSTOM_API_KEY, "")
            .putString(KEY_CUSTOM_BACKEND_URL, "")
            .apply()
        _creditsFlow.value = DEFAULT_STARTER_CREDITS
        _languageFlow.value = "hi"
    }
}
