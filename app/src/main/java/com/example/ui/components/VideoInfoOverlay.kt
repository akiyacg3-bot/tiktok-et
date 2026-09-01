package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoPost
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokWhite

@Composable
fun VideoInfoOverlay(
    video: VideoPost,
    onUserClick: () -> Unit,
    onOpenGeminiHashtags: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth(0.78f)
            .padding(start = 16.dp, bottom = 24.dp)
            .testTag("video_info_overlay")
    ) {
        // Creator Username & Handle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onUserClick)
                .testTag("video_creator_tag")
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = video.username,
                        color = TikTokWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified Creator",
                        tint = TikTokCyan,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = video.userHandle,
                    color = TikTokWhite.copy(alpha = 0.75f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Gemini Trending Ethiopian Hashtags Pill Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF4F46E5).copy(alpha = 0.85f), Color(0xFFDB2777).copy(alpha = 0.85f))
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                .clickable { onOpenGeminiHashtags() }
                .padding(horizontal = 9.dp, vertical = 4.dp)
                .testTag("gemini_ethiopian_tags_pill")
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "🇪🇹 Ethio Hashtags (Gemini AI)",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Video Description with colored clickable #hashtags and @mentions
        val annotatedCaption = remember(video.description) {
            buildAnnotatedString {
                val words = video.description.split(" ")
                words.forEachIndexed { index, word ->
                    if (word.startsWith("#") || word.startsWith("@")) {
                        withStyle(
                            style = SpanStyle(
                                color = TikTokCyan,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(word)
                        }
                    } else {
                        withStyle(style = SpanStyle(color = Color(0xFFF4F4F5))) {
                            append(word)
                        }
                    }
                    if (index < words.size - 1) append(" ")
                }
            }
        }

        Text(
            text = annotatedCaption,
            fontSize = 13.sp,
            lineHeight = 17.sp,
            maxLines = if (isExpanded) 8 else 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .testTag("video_caption_text")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Audio Music Track Title Ticker
        AudioTickerBar(
            title = "${video.musicTitle} • ${video.musicAuthor}",
            modifier = Modifier.testTag("audio_ticker_bar")
        )
    }
}

@Composable
fun AudioTickerBar(
    title: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_marquee")
    val offsetProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -120f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "marquee"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.35f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = "Music",
            tint = TikTokWhite,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .width(180.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Row(
                modifier = Modifier.offset(x = offsetProgress.dp)
            ) {
                Text(
                    text = "$title     •     $title",
                    color = TikTokWhite.copy(alpha = 0.95f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = "Audio visualizer",
            tint = TikTokCyan,
            modifier = Modifier.size(13.dp)
        )
    }
}
