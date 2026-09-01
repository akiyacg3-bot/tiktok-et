package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.model.VideoPost
import com.example.model.VideoThemeStyle
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class TapHeartParticle(
    val id: Long,
    val offset: Offset,
    val rotation: Float,
    val scale: Float = 1f
)

@Composable
fun VideoPlayerPlaceholder(
    video: VideoPost,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onDoubleTapLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPlayPauseIcon by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableFloatStateOf(0.35f) }
    val heartParticles = remember { mutableStateListOf<TapHeartParticle>() }
    val scope = rememberCoroutineScope()

    // Smooth simulated video playback progress
    val infiniteTransition = rememberInfiniteTransition(label = "video_progress")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "playback"
    )

    // Visual theme gradient colors
    val gradientColors = remember(video.videoTheme) {
        when (video.videoTheme) {
            VideoThemeStyle.CYBERPUNK_NEON -> listOf(
                Color(0xFF0F0C29),
                Color(0xFF302B63),
                Color(0xFF24243E),
                Color(0xFF050505)
            )
            VideoThemeStyle.SUNSET_BEACH -> listOf(
                Color(0xFFFF512F),
                Color(0xFFDD2476),
                Color(0xFF4A00E0),
                Color(0xFF110826)
            )
            VideoThemeStyle.NATURE_FOREST -> listOf(
                Color(0xFF134E5E),
                Color(0xFF71B280),
                Color(0xFF0C2B1D),
                Color(0xFF04100B)
            )
            VideoThemeStyle.URBAN_DANCE -> listOf(
                Color(0xFF8A2387),
                Color(0xFFE94057),
                Color(0xFFF27121),
                Color(0xFF0B0914)
            )
            VideoThemeStyle.TECH_CODING -> listOf(
                Color(0xFF000428),
                Color(0xFF004E92),
                Color(0xFF0A192F),
                Color(0xFF020C1B)
            )
            VideoThemeStyle.COZY_COFFEE -> listOf(
                Color(0xFF3E2723),
                Color(0xFF5D4037),
                Color(0xFF8D6E63),
                Color(0xFF1B120C)
            )
            VideoThemeStyle.GAMING_ARCADE -> listOf(
                Color(0xFF200122),
                Color(0xFF6F0000),
                Color(0xFF3A0042),
                Color(0xFF09000C)
            )
        }
    }

    // Dynamic wave / particle animations on video canvas
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(video.id) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        onDoubleTapLike()
                        val newParticle = TapHeartParticle(
                            id = System.currentTimeMillis() + Random.nextLong(1000),
                            offset = offset,
                            rotation = Random.nextFloat() * 40f - 20f
                        )
                        heartParticles.add(newParticle)
                    },
                    onTap = {
                        onTogglePlay()
                        showPlayPauseIcon = true
                        scope.launch {
                            delay(600)
                            showPlayPauseIcon = false
                        }
                    }
                )
            }
            .testTag("video_player_placeholder_${video.id}")
    ) {
        // Video canvas background with animated waves and graphics
        VideoCanvasBackdrop(
            theme = video.videoTheme,
            isPlaying = isPlaying,
            modifier = Modifier.fillMaxSize()
        )

        // Top & Bottom gradient scrims for maximum text readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.65f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.45f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Animated Play/Pause indicator on tap
        if (showPlayPauseIcon || !isPlaying) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause Indicator",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        // Double-Tap Floating Hearts Animation
        heartParticles.forEach { particle ->
            HeartBurstEffect(
                particle = particle,
                onAnimationEnd = { heartParticles.remove(particle) }
            )
        }

        // Subtle bottom video scrub/progress line
        LinearProgressIndicator(
            progress = { if (isPlaying) animatedProgress else playbackProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .align(Alignment.BottomCenter),
            color = Color.White.copy(alpha = 0.85f),
            trackColor = Color.White.copy(alpha = 0.25f)
        )
    }
}

@Composable
fun HeartBurstEffect(
    particle: TapHeartParticle,
    onAnimationEnd: () -> Unit
) {
    val scale = remember { Animatable(0.2f) }
    val alpha = remember { Animatable(1f) }
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(particle.id) {
        // Fast pop & float up
        launch {
            scale.animateTo(
                targetValue = 1.35f,
                animationSpec = tween(280, easing = FastOutSlowInEasing)
            )
            scale.animateTo(
                targetValue = 1.1f,
                animationSpec = tween(200)
            )
        }
        launch {
            offsetY.animateTo(
                targetValue = -90f,
                animationSpec = tween(650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            delay(350)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(300)
            )
            onAnimationEnd()
        }
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = particle.offset.x.toInt() - 40,
                    y = (particle.offset.y + offsetY.value).toInt() - 40
                )
            }
            .scale(scale.value)
            .alpha(alpha.value)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Floating Like Heart",
            tint = TikTokRed,
            modifier = Modifier.size(72.dp)
        )
    }
}

@Composable
fun VideoCanvasBackdrop(
    theme: VideoThemeStyle,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "canvas_backdrop")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_backdrop"
    )

    val gradientColors = remember(theme) {
        when (theme) {
            VideoThemeStyle.CYBERPUNK_NEON -> listOf(
                Color(0xFF0F0C29),
                Color(0xFF302B63),
                Color(0xFF24243E),
                Color(0xFF050505)
            )
            VideoThemeStyle.SUNSET_BEACH -> listOf(
                Color(0xFFFF512F),
                Color(0xFFDD2476),
                Color(0xFF4A00E0),
                Color(0xFF110826)
            )
            VideoThemeStyle.NATURE_FOREST -> listOf(
                Color(0xFF134E5E),
                Color(0xFF71B280),
                Color(0xFF0C2B1D),
                Color(0xFF04100B)
            )
            VideoThemeStyle.URBAN_DANCE -> listOf(
                Color(0xFF8A2387),
                Color(0xFFE94057),
                Color(0xFFF27121),
                Color(0xFF0B0914)
            )
            VideoThemeStyle.TECH_CODING -> listOf(
                Color(0xFF000428),
                Color(0xFF004E92),
                Color(0xFF0A192F),
                Color(0xFF020C1B)
            )
            VideoThemeStyle.COZY_COFFEE -> listOf(
                Color(0xFF3E2723),
                Color(0xFF5D4037),
                Color(0xFF8D6E63),
                Color(0xFF1B120C)
            )
            VideoThemeStyle.GAMING_ARCADE -> listOf(
                Color(0xFF200122),
                Color(0xFF6F0000),
                Color(0xFF3A0042),
                Color(0xFF09000C)
            )
        }
    }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRect(
            brush = Brush.verticalGradient(
                colors = gradientColors,
                startY = 0f,
                endY = canvasHeight
            )
        )

        val cx1 = canvasWidth * 0.5f + cos(pulsePhase.toDouble()).toFloat() * (canvasWidth * 0.25f)
        val cy1 = canvasHeight * 0.45f + sin(pulsePhase.toDouble()).toFloat() * (canvasHeight * 0.15f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    TikTokCyan.copy(alpha = 0.22f),
                    Color.Transparent
                ),
                center = Offset(cx1, cy1),
                radius = canvasWidth * 0.65f
            ),
            center = Offset(cx1, cy1),
            radius = canvasWidth * 0.65f
        )

        val cx2 = canvasWidth * 0.5f - cos((pulsePhase * 0.8).toDouble()).toFloat() * (canvasWidth * 0.2f)
        val cy2 = canvasHeight * 0.55f - sin((pulsePhase * 0.8).toDouble()).toFloat() * (canvasHeight * 0.18f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    TikTokRed.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(cx2, cy2),
                radius = canvasWidth * 0.7f
            ),
            center = Offset(cx2, cy2),
            radius = canvasWidth * 0.7f
        )

        val barCount = 28
        val barWidth = canvasWidth / (barCount * 1.6f)
        val spacing = barWidth * 0.6f
        val startX = (canvasWidth - (barCount * (barWidth + spacing))) / 2f
        val baseCenterY = canvasHeight * 0.52f

        for (i in 0 until barCount) {
            val waveOffset = (i * 0.35f + pulsePhase).toDouble()
            val barHeight = if (isPlaying) {
                (sin(waveOffset) * 60f + cos((i * 0.5 + pulsePhase * 1.5).toDouble()) * 40f).toFloat().coerceIn(15f, 130f)
            } else {
                20f
            }
            val bx = startX + i * (barWidth + spacing)
            drawRoundRect(
                color = Color.White.copy(alpha = 0.18f + (barHeight / 130f) * 0.22f),
                topLeft = Offset(bx, baseCenterY - barHeight / 2f),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
        }
    }
}
