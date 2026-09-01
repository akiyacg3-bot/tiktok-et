package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.VideoPost
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RightActionButtons(
    video: VideoPost,
    isFollowing: Boolean,
    onFollowToggle: () -> Unit,
    onLikeToggle: () -> Unit,
    onCommentClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onShareClick: () -> Unit,
    onGiftClick: () -> Unit,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val heartScale = remember { Animatable(1f) }

    LaunchedEffect(video.isLiked) {
        if (video.isLiked) {
            heartScale.animateTo(
                targetValue = 1.3f,
                animationSpec = spring(dampingRatio = 0.4f, stiffness = 600f)
            )
            heartScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.6f)
            )
        }
    }

    Column(
        modifier = modifier
            .padding(end = 12.dp, bottom = 16.dp)
            .testTag("right_action_buttons_column"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Creator Profile Avatar + Follow Badge (Professional Polish)
        CreatorAvatarWithFollow(
            username = video.username,
            isFollowing = isFollowing,
            onFollowToggle = onFollowToggle,
            onAvatarClick = onAvatarClick
        )

        // 2. Like Button
        ActionButtonItem(
            icon = {
                Icon(
                    imageVector = if (video.isLiked) Icons.Default.Favorite else Icons.Default.Favorite,
                    contentDescription = if (video.isLiked) "Unlike" else "Like",
                    tint = if (video.isLiked) TikTokRed else TikTokWhite,
                    modifier = Modifier
                        .size(30.dp)
                        .scale(heartScale.value)
                )
            },
            label = MockData.formatCount(video.likesCount),
            onClick = {
                onLikeToggle()
                scope.launch {
                    heartScale.snapTo(0.85f)
                    heartScale.animateTo(
                        targetValue = 1.3f,
                        animationSpec = spring(dampingRatio = 0.4f)
                    )
                    heartScale.animateTo(1f)
                }
            },
            testTag = "like_button_${video.id}"
        )

        // 3. Comment Button
        ActionButtonItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ModeComment,
                    contentDescription = "Comments",
                    tint = TikTokWhite,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = MockData.formatCount(video.commentsCount),
            onClick = onCommentClick,
            testTag = "comment_button_${video.id}"
        )

        // 4. Bookmark / Favorite Button
        ActionButtonItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Bookmark",
                    tint = if (video.isBookmarked) TikTokCoinGold else TikTokWhite,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = MockData.formatCount(video.bookmarksCount),
            onClick = onBookmarkToggle,
            testTag = "bookmark_button_${video.id}"
        )

        // 5. Share Button
        ActionButtonItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = TikTokWhite,
                    modifier = Modifier.size(30.dp)
                )
            },
            label = MockData.formatCount(video.sharesCount),
            onClick = onShareClick,
            testTag = "share_button_${video.id}"
        )

        // 6. Virtual Gifts Button (Yellow to Orange gradient pill)
        VirtualGiftActionButton(
            onClick = onGiftClick,
            testTag = "gift_button_${video.id}"
        )

        // 7. Rotating Music Disc with floating musical notes
        RotatingMusicDisc(
            author = video.musicAuthor,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun CreatorAvatarWithFollow(
    username: String,
    isFollowing: Boolean,
    onFollowToggle: () -> Unit,
    onAvatarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onAvatarClick
            )
            .testTag("creator_avatar"),
        contentAlignment = Alignment.Center
    ) {
        // Avatar circle with border-2 border-white & gradient bg
        Box(
            modifier = Modifier
                .size(44.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF818CF8), Color(0xFFA855F7))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            val initial = username.firstOrNull()?.uppercase() ?: "T"
            Text(
                text = initial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        // Follow Plus / Checked badge at bottom center of avatar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 6.dp)
                .size(20.dp)
                .border(2.dp, Color.Black, CircleShape)
                .clip(CircleShape)
                .background(if (isFollowing) Color.White else TikTokRed)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onFollowToggle
                )
                .testTag("follow_toggle_button"),
            contentAlignment = Alignment.Center
        ) {
            if (isFollowing) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Following",
                    tint = TikTokRed,
                    modifier = Modifier.size(12.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Follow",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

@Composable
fun ActionButtonItem(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        icon()
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TikTokWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun VirtualGiftActionButton(
    onClick: () -> Unit,
    testTag: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gift_glow")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFACC15),
                            Color(0xFFF97316)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Redeem,
                contentDescription = "Virtual Gifts",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Gifts",
            color = TikTokWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun RotatingMusicDisc(
    author: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val noteFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "note"
    )

    Box(
        modifier = modifier
            .size(48.dp)
            .testTag("rotating_music_disc"),
        contentAlignment = Alignment.Center
    ) {
        // Floating animated music notes drifting up & left
        if (noteFloat > 0.05f) {
            val noteX = -16.dp - (noteFloat * 18).dp
            val noteY = -10.dp - (noteFloat * 42).dp
            val noteAlpha = (1f - noteFloat).coerceIn(0f, 1f)
            val noteScale = (0.6f + noteFloat * 0.5f).coerceIn(0f, 1f)

            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = TikTokWhite.copy(alpha = noteAlpha),
                modifier = Modifier
                    .offset(x = noteX, y = noteY)
                    .scale(noteScale)
                    .size(15.dp)
            )
        }

        // Vinyl Disc styled per Professional Polish theme
        Box(
            modifier = Modifier
                .size(44.dp)
                .rotate(rotation)
                .clip(CircleShape)
                .background(Color(0xFF27272A))
                .border(3.5.dp, Color(0xFF3F3F46).copy(alpha = 0.5f), CircleShape)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            // Inner decorative album rotated square/circle
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .rotate(45f)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFA5B4FC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music Track",
                    tint = Color(0xFF1E1B4B),
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}
