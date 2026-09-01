package com.example.data

import com.example.model.VirtualGift
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

data class LiveChatMessage(
    val id: String,
    val senderName: String,
    val senderHandle: String,
    val message: String,
    val isSystem: Boolean = false,
    val badge: String? = null
)

data class LiveStreamState(
    val isLiveActive: Boolean = false,
    val title: String = "✨ Hanging out & Chilling with Addis Vibes!",
    val category: String = "Chat & Chill",
    val viewerCount: Int = 1240,
    val likesCount: Long = 4820,
    val diamondsEarned: Long = 350,
    val durationSeconds: Long = 0,
    val messages: List<LiveChatMessage> = emptyList()
)

data class UserAgeProfile(
    val birthYear: Int = 2003,
    val birthMonth: Int = 5,
    val birthDay: Int = 15,
    val isAgeVerified: Boolean = false,
    val verificationTag: String = "UNVERIFIED"
) {
    val calculatedAge: Int
        get() {
            // Reference year 2026
            val currentYear = 2026
            val currentMonth = 9
            val currentDay = 1
            var age = currentYear - birthYear
            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
                age--
            }
            return age
        }

    val isEligibleForLive: Boolean
        get() = calculatedAge >= 16
}

object LiveStreamManager {
    // Current user's age profile (Default: 2003, age 23, unverified initially to show verification modal)
    private val _userProfile = MutableStateFlow(
        UserAgeProfile(
            birthYear = 2003,
            birthMonth = 5,
            birthDay = 15,
            isAgeVerified = false,
            verificationTag = "UNVERIFIED"
        )
    )
    val userProfile: StateFlow<UserAgeProfile> = _userProfile.asStateFlow()

    // Live Stream state
    private val _liveStreamState = MutableStateFlow(LiveStreamState())
    val liveStreamState: StateFlow<LiveStreamState> = _liveStreamState.asStateFlow()

    // Restriction message constant
    const val RESTRICTION_MESSAGE = "Live streaming is only available for creators aged 16 and above."

    fun updateBirthDate(year: Int, month: Int, day: Int) {
        val tempProfile = _userProfile.value.copy(
            birthYear = year,
            birthMonth = month,
            birthDay = day
        )
        val isEligible = tempProfile.isEligibleForLive
        _userProfile.value = tempProfile.copy(
            verificationTag = if (isEligible) "VERIFIED_16_PLUS" else "RESTRICTED_UNDER_16"
        )
    }

    fun verifyAge(year: Int, month: Int, day: Int): Boolean {
        val tempProfile = _userProfile.value.copy(
            birthYear = year,
            birthMonth = month,
            birthDay = day
        )
        val isEligible = tempProfile.isEligibleForLive
        _userProfile.value = tempProfile.copy(
            isAgeVerified = true,
            verificationTag = if (isEligible) "VERIFIED_16_PLUS" else "RESTRICTED_UNDER_16"
        )
        return isEligible
    }

    fun setAgePreset(age: Int) {
        val birthYear = 2026 - age
        verifyAge(birthYear, 1, 1)
    }

    fun startLiveSession(title: String, category: String): Boolean {
        if (!_userProfile.value.isEligibleForLive) {
            return false
        }
        _liveStreamState.value = LiveStreamState(
            isLiveActive = true,
            title = if (title.isNotBlank()) title else "✨ Live Streaming on TikTok Addis!",
            category = category,
            viewerCount = 184,
            likesCount = 350,
            diamondsEarned = 0,
            durationSeconds = 0,
            messages = listOf(
                LiveChatMessage(
                    id = "msg_sys_1",
                    senderName = "TikTok LIVE Safety",
                    senderHandle = "@system",
                    message = "Welcome to LIVE! Age verification passed (16+). Keep discussions friendly and respectful.",
                    isSystem = true
                ),
                LiveChatMessage(
                    id = "msg_1",
                    senderName = "Hanna M.",
                    senderHandle = "@hanna_m",
                    message = "Selam!! We are so excited for your stream! ❤️",
                    badge = "VIP"
                ),
                LiveChatMessage(
                    id = "msg_2",
                    senderName = "Dawit K.",
                    senderHandle = "@dawit_ethio",
                    message = "Addis vibe is unmatched tonight 🔥✨",
                    badge = "Top Fan"
                )
            )
        )
        return true
    }

    fun endLiveSession() {
        _liveStreamState.value = _liveStreamState.value.copy(isLiveActive = false)
    }

    fun addLiveLike() {
        _liveStreamState.value = _liveStreamState.value.copy(
            likesCount = _liveStreamState.value.likesCount + 1
        )
    }

    fun sendLiveMessage(sender: String, message: String) {
        val newMsg = LiveChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = sender,
            senderHandle = "@you",
            message = message
        )
        _liveStreamState.value = _liveStreamState.value.copy(
            messages = _liveStreamState.value.messages + newMsg
        )
    }

    fun receiveViewerGift(gift: VirtualGift, count: Int = 1) {
        val addedDiamonds = gift.coins.toLong() * count
        val giftMsg = LiveChatMessage(
            id = "gift_${System.currentTimeMillis()}",
            senderName = "Top Viewer",
            senderHandle = "@fan_club",
            message = "sent ${gift.name} ${gift.iconEmoji} x$count (+$addedDiamonds 💎)",
            isSystem = true
        )
        _liveStreamState.value = _liveStreamState.value.copy(
            diamondsEarned = _liveStreamState.value.diamondsEarned + addedDiamonds,
            messages = _liveStreamState.value.messages + giftMsg,
            viewerCount = _liveStreamState.value.viewerCount + (1..5).random()
        )
    }
}
