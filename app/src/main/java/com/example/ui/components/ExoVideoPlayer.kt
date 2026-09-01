package com.example.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.model.VideoPost
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TapHeartItem(
    val id: Long,
    val offset: Offset,
    val scale: Animatable<Float, *>,
    val alpha: Animatable<Float, *>,
    val rotation: Float
)

@OptIn(UnstableApi::class)
@Composable
fun ExoVideoPlayer(
    video: VideoPost,
    isPlaying: Boolean,
    isCurrentPage: Boolean,
    onTogglePlay: () -> Unit,
    onDoubleTapLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isBuffering by remember { mutableStateOf(true) }
    var playbackError by remember { mutableStateOf(false) }
    var showPlayIcon by remember { mutableStateOf(false) }

    val tapHearts = remember { mutableStateListOf<TapHeartItem>() }

    // Media3 ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            volume = 1f
        }
    }

    // Set up video URL & listener
    LaunchedEffect(video.videoUrl) {
        if (video.videoUrl.isNotBlank()) {
            try {
                playbackError = false
                val mediaItem = MediaItem.fromUri(video.videoUrl)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
            } catch (e: Exception) {
                playbackError = true
            }
        } else {
            playbackError = true
        }
    }

    // Attach ExoPlayer playback listener
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> isBuffering = true
                    Player.STATE_READY -> isBuffering = false
                    Player.STATE_ENDED -> isBuffering = false
                    Player.STATE_IDLE -> isBuffering = false
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                isBuffering = false
                playbackError = true
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Play/Pause lifecycle based on current page visibility & user toggle
    LaunchedEffect(isPlaying, isCurrentPage) {
        if (isCurrentPage && isPlaying) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
            showPlayIcon = false
        } else {
            exoPlayer.playWhenReady = false
            exoPlayer.pause()
            if (isCurrentPage && !isPlaying) {
                showPlayIcon = true
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onTogglePlay()
                    },
                    onDoubleTap = { tapOffset ->
                        onDoubleTapLike()
                        // Spawn floating heart at tap location
                        val newHeart = TapHeartItem(
                            id = System.currentTimeMillis() + (0..1000).random(),
                            offset = tapOffset,
                            scale = Animatable(0.2f),
                            alpha = Animatable(1f),
                            rotation = (-25..25).random().toFloat()
                        )
                        tapHearts.add(newHeart)

                        scope.launch {
                            launch {
                                newHeart.scale.animateTo(
                                    targetValue = 1.3f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = 800f
                                    )
                                )
                            }
                            launch {
                                delay(400)
                                newHeart.alpha.animateTo(0f, animationSpec = tween(350))
                                tapHearts.remove(newHeart)
                            }
                        }
                    }
                )
            }
            .testTag("exo_video_player_${video.id}")
    ) {
        if (!playbackError && video.videoUrl.isNotBlank()) {
            // Real Media3 PlayerView
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Aesthetic dynamic fallback canvas
            VideoCanvasBackdrop(
                theme = video.videoTheme,
                isPlaying = isPlaying && isCurrentPage,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Top and bottom subtle gradient vignettes for overlay contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.55f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Buffering Indicator
        if (isBuffering && isCurrentPage) {
            CircularProgressIndicator(
                color = TikTokRed,
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.Center)
                    .testTag("video_buffering_indicator")
            )
        }

        // Center Play/Pause icon badge
        if (showPlayIcon || (!isPlaying && isCurrentPage)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        // Double Tap Floating Hearts
        tapHearts.forEach { heart ->
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = TikTokRed.copy(alpha = heart.alpha.value),
                modifier = Modifier
                    .offset(
                        x = (heart.offset.x / context.resources.displayMetrics.density).dp - 36.dp,
                        y = (heart.offset.y / context.resources.displayMetrics.density).dp - 36.dp
                    )
                    .scale(heart.scale.value)
                    .size(72.dp)
            )
        }
    }
}
