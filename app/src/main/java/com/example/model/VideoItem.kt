package com.example.model

data class VideoPost(
    val id: String,
    val username: String,
    val userHandle: String,
    val avatarUrl: String = "",
    val videoUrl: String = "",
    val description: String,
    val musicTitle: String,
    val musicAuthor: String,
    val likesCount: Long,
    val commentsCount: Long,
    val sharesCount: Long,
    val bookmarksCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val isFollowing: Boolean = false,
    val videoTheme: VideoThemeStyle,
    val tags: List<String> = listOf("fyp", "trending", "viral")
)

enum class VideoThemeStyle {
    CYBERPUNK_NEON,
    SUNSET_BEACH,
    NATURE_FOREST,
    URBAN_DANCE,
    TECH_CODING,
    COZY_COFFEE,
    GAMING_ARCADE
}

data class CommentItem(
    val id: String,
    val username: String,
    val userHandle: String,
    val commentText: String,
    val timeAgo: String,
    val likesCount: Long,
    val isLiked: Boolean = false,
    val isCreatorLiked: Boolean = false
)

data class VirtualGift(
    val id: String,
    val name: String,
    val coins: Int,
    val iconEmoji: String,
    val description: String,
    val animationColorHex: Long
)

data class ActiveGiftAnimation(
    val id: Long,
    val gift: VirtualGift,
    val senderName: String,
    val recipientName: String,
    val comboCount: Int = 1
)

data class TelebirrPackage(
    val id: String,
    val coins: Int,
    val priceEtb: Int,
    val bonusLabel: String = "",
    val isPopular: Boolean = false
)

data class GiftTransaction(
    val id: String,
    val gift: VirtualGift,
    val senderName: String,
    val recipientHandle: String,
    val comboCount: Int,
    val totalCoins: Int,
    val timestamp: String
)

enum class MonetizationStatus {
    IN_PROGRESS,
    ELIGIBLE,
    APPROVED
}

data class CreatorWallet(
    val creatorHandle: String,
    val creatorName: String,
    val accumulatedCoins: Long = 0,
    val totalGiftsReceived: Int = 0,
    val followerCount: Int = 0,
    val totalVideoViews: Long = 0,
    val isMonetized: Boolean = false,
    val transactions: List<GiftTransaction> = emptyList()
) {
    val targetFollowers: Int = 5000
    val targetViews: Long = 50000
    val isEligibleToApply: Boolean get() = followerCount >= targetFollowers && totalVideoViews >= targetViews
    val followerProgress: Float get() = (followerCount.toFloat() / targetFollowers.toFloat()).coerceIn(0f, 1f)
    val viewsProgress: Float get() = (totalVideoViews.toFloat() / targetViews.toFloat()).coerceIn(0f, 1f)
}

enum class TikTokTab {
    HOME,
    FRIENDS,
    CREATE,
    INBOX,
    PROFILE
}
