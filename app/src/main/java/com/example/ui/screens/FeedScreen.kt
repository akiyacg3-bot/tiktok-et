package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LiveStreamManager
import com.example.data.MockData
import com.example.data.MonetizationManager
import com.example.model.ActiveGiftAnimation
import com.example.model.VideoPost
import com.example.model.VirtualGift
import com.example.ui.components.AgeVerificationDialog
import com.example.ui.components.CommentBottomSheet
import com.example.ui.components.ExoVideoPlayer
import com.example.ui.components.FeedTab
import com.example.ui.components.GeminiHashtagBottomSheet
import com.example.ui.components.GiftAnimationOverlay
import com.example.ui.components.GiftBottomSheet
import com.example.ui.components.LiveStreamRestrictionDialog
import com.example.ui.components.RightActionButtons
import com.example.ui.components.ShareBottomSheet
import com.example.ui.components.TopHeaderBar
import com.example.ui.components.VideoInfoOverlay
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedScreen(
    onNavigateToProfile: () -> Unit,
    onGoLiveDirect: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videos = remember { mutableStateListOf<VideoPost>().apply { addAll(MockData.initialVideos) } }
    val pagerState = rememberPagerState(pageCount = { videos.size })

    var selectedFeedTab by remember { mutableStateOf(FeedTab.FOR_YOU) }
    var isPlaying by remember { mutableStateOf(true) }

    // Active bottom sheets
    var showCommentSheetForVideo by remember { mutableStateOf<VideoPost?>(null) }
    var showGiftSheetForVideo by remember { mutableStateOf<VideoPost?>(null) }
    var showShareSheetForVideo by remember { mutableStateOf<VideoPost?>(null) }
    var showGeminiHashtagsForVideo by remember { mutableStateOf<VideoPost?>(null) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showLiveDialog by remember { mutableStateOf(false) }
    var showAgeVerificationModal by remember { mutableStateOf(false) }
    var showRestrictionModal by remember { mutableStateOf(false) }

    val userProfile by LiveStreamManager.userProfile.collectAsState()

    // User Coins (Monetization & Virtual Gifting)
    val userCoins by MonetizationManager.userCoins.collectAsState()
    var activeGiftAnimation by remember { mutableStateOf<ActiveGiftAnimation?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .testTag("feed_screen")
    ) {
        // Vertical PageView for Short Videos
        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .testTag("vertical_video_pager")
        ) { page ->
            val video = videos[page]
            val isCurrentPage = pagerState.currentPage == page

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("video_page_$page")
            ) {
                // 1. Media3 ExoPlayer with real MP4 video URLs, auto-looping, and tap gestures
                ExoVideoPlayer(
                    video = video,
                    isPlaying = isPlaying,
                    isCurrentPage = isCurrentPage,
                    onTogglePlay = { isPlaying = !isPlaying },
                    onDoubleTapLike = {
                        if (!video.isLiked) {
                            videos[page] = video.copy(
                                isLiked = true,
                                likesCount = video.likesCount + 1
                            )
                        }
                    }
                )

                // 2. Video Info Overlay (Bottom Left: @handle, description, audio track, Gemini Hashtags trigger)
                VideoInfoOverlay(
                    video = video,
                    onUserClick = onNavigateToProfile,
                    onOpenGeminiHashtags = { showGeminiHashtagsForVideo = video },
                    modifier = Modifier.align(Alignment.BottomStart)
                )

                // 3. Right-Side Interactive Action Buttons (Avatar, Like, Comment, Bookmark, Share, Gifts, Rotating Disc)
                RightActionButtons(
                    video = video,
                    isFollowing = video.isFollowing,
                    onFollowToggle = {
                        val newFollowState = !video.isFollowing
                        videos[page] = video.copy(isFollowing = newFollowState)
                        val message = if (newFollowState) "Followed ${video.username}!" else "Unfollowed ${video.username}"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    onLikeToggle = {
                        val newLiked = !video.isLiked
                        val newCount = if (newLiked) video.likesCount + 1 else video.likesCount - 1
                        videos[page] = video.copy(
                            isLiked = newLiked,
                            likesCount = newCount
                        )
                    },
                    onCommentClick = {
                        showCommentSheetForVideo = video
                    },
                    onBookmarkToggle = {
                        val newBookmarked = !video.isBookmarked
                        val newCount = if (newBookmarked) video.bookmarksCount + 1 else video.bookmarksCount - 1
                        videos[page] = video.copy(
                            isBookmarked = newBookmarked,
                            bookmarksCount = newCount
                        )
                        val message = if (newBookmarked) "Added to Favorites" else "Removed from Favorites"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    onShareClick = {
                        showShareSheetForVideo = video
                    },
                    onGiftClick = {
                        showGiftSheetForVideo = video
                    },
                    onAvatarClick = onNavigateToProfile,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        // Top Header Bar: LIVE | Following | Friends | For You | Search
        TopHeaderBar(
            selectedTab = selectedFeedTab,
            onTabSelected = { tab ->
                selectedFeedTab = tab
                Toast.makeText(context, "Switched to ${tab.name.replace("_", " ")} feed", Toast.LENGTH_SHORT).show()
            },
            onLiveClick = { showLiveDialog = true },
            onSearchClick = { showSearchDialog = true },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Virtual Gift Full Screen Floating Animation Overlay
        GiftAnimationOverlay(
            activeGift = activeGiftAnimation,
            onDismiss = { activeGiftAnimation = null }
        )

        // Interactive Comment Bottom Sheet
        showCommentSheetForVideo?.let { targetVideo ->
            CommentBottomSheet(
                commentsCount = targetVideo.commentsCount,
                onDismiss = { showCommentSheetForVideo = null }
            )
        }

        // Virtual Gifts Modal Sheet
        showGiftSheetForVideo?.let { targetVideo ->
            GiftBottomSheet(
                recipientName = targetVideo.userHandle,
                userCoins = userCoins,
                onSendGift = { gift, combo ->
                    val success = MonetizationManager.sendGiftToCreator(
                        gift = gift,
                        combo = combo,
                        creatorHandle = targetVideo.userHandle,
                        creatorName = targetVideo.username,
                        senderName = "You"
                    )
                    if (success) {
                        activeGiftAnimation = ActiveGiftAnimation(
                            id = System.currentTimeMillis(),
                            gift = gift,
                            senderName = "You",
                            recipientName = targetVideo.userHandle,
                            comboCount = combo
                        )
                        showGiftSheetForVideo = null
                    }
                },
                onRechargeCoins = { additionalCoins ->
                    Toast.makeText(context, "Telebirr payment successful! Added $additionalCoins coins.", Toast.LENGTH_SHORT).show()
                },
                onDismiss = { showGiftSheetForVideo = null }
            )
        }

        // Gemini Trending Ethiopian Hashtags Bottom Sheet
        showGeminiHashtagsForVideo?.let { targetVideo ->
            GeminiHashtagBottomSheet(
                video = targetVideo,
                onApplyHashtags = { newTags ->
                    val index = videos.indexOfFirst { it.id == targetVideo.id }
                    if (index != -1) {
                        val baseDesc = targetVideo.description.split("#").first().trim()
                        val updatedDesc = "$baseDesc " + newTags.joinToString(" ")
                        videos[index] = targetVideo.copy(
                            description = updatedDesc,
                            tags = newTags
                        )
                    }
                },
                onDismiss = { showGeminiHashtagsForVideo = null }
            )
        }

        // Share Bottom Sheet
        showShareSheetForVideo?.let { targetVideo ->
            ShareBottomSheet(
                video = targetVideo,
                onDismiss = { showShareSheetForVideo = null }
            )
        }

        // Search Dialog
        if (showSearchDialog) {
            SearchDialog(onDismiss = { showSearchDialog = false })
        }

        // LIVE Stream Preview Dialog
        if (showLiveDialog) {
            LivePreviewDialog(
                onDismiss = { showLiveDialog = false },
                onWatchLive = {
                    showLiveDialog = false
                    Toast.makeText(context, "Streaming top creator in Addis... 🔴", Toast.LENGTH_SHORT).show()
                },
                onGoLive = {
                    showLiveDialog = false
                    if (!userProfile.isAgeVerified) {
                        showAgeVerificationModal = true
                    } else if (!userProfile.isEligibleForLive) {
                        Toast.makeText(context, LiveStreamManager.RESTRICTION_MESSAGE, Toast.LENGTH_LONG).show()
                        showRestrictionModal = true
                    } else {
                        onGoLiveDirect()
                    }
                }
            )
        }

        // Age Verification Modal
        if (showAgeVerificationModal) {
            AgeVerificationDialog(
                onDismiss = { showAgeVerificationModal = false },
                onVerifiedSuccess = {
                    showAgeVerificationModal = false
                    onGoLiveDirect()
                }
            )
        }

        // Friendly Restriction Dialog
        if (showRestrictionModal) {
            LiveStreamRestrictionDialog(
                onDismiss = { showRestrictionModal = false },
                onOpenAgeVerification = {
                    showRestrictionModal = false
                    showAgeVerificationModal = true
                }
            )
        }
    }
}

@Composable
fun SearchDialog(onDismiss: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val trendingTags = listOf("#jetpackcompose", "#dancechallenge", "#synthwave", "#codinglife", "#tokyovibes")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E2024),
        title = {
            Text(
                text = "Search TikTok",
                color = TikTokWhite
            )
        },
        text = {
            androidx.compose.foundation.layout.Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search creators, sounds, tags...", color = Color.Gray) },
                    modifier = Modifier.fillMaxSize().testTag("search_text_input")
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Search", color = TikTokRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Composable
fun LivePreviewDialog(
    onDismiss: () -> Unit,
    onWatchLive: () -> Unit = onDismiss,
    onGoLive: () -> Unit = {}
) {
    val userProfile by LiveStreamManager.userProfile.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E2024),
        title = {
            Text(
                text = "🔴 TikTok LIVE Streams",
                color = TikTokWhite
            )
        },
        text = {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Discover top creators streaming right now with interactive chat, gifts, and Addis vibes!",
                    color = TikTokWhite.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )

                // Age eligibility status note
                Text(
                    text = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) "Your Account: Verified (16+) ✅ Ready to Broadcast"
                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) "Your Account: Under 16 🔒 (Broadcast restricted)"
                    else "Creator requirement: Age 16+ verified to broadcast live",
                    color = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676)
                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) Color(0xFFFF8A80)
                    else TikTokCyan,
                    fontSize = 11.sp
                )
            }
        },
        confirmButton = {
            androidx.compose.foundation.layout.Row {
                TextButton(onClick = onWatchLive) {
                    Text("Watch LIVE", color = TikTokGray)
                }
                TextButton(onClick = onGoLive) {
                    Text("Go LIVE 🔴", color = TikTokRed, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.Gray)
            }
        }
    )
}
