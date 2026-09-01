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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActiveGiftAnimation
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GiftAnimationOverlay(
    activeGift: ActiveGiftAnimation?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (activeGift == null) return

    val scaleAnim = remember { Animatable(0.2f) }
    val bannerSlide = remember { Animatable(-100f) }
    val overlayAlpha = remember { Animatable(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val sparkleAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkle"
    )

    LaunchedEffect(activeGift.id) {
        launch {
            bannerSlide.animateTo(0f, spring(dampingRatio = 0.6f))
        }
        launch {
            scaleAnim.animateTo(1.6f, tween(400, easing = FastOutSlowInEasing))
            scaleAnim.animateTo(1.15f, spring(dampingRatio = 0.4f))
        }
        launch {
            delay(2800)
            overlayAlpha.animateTo(0f, tween(500))
            onDismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(overlayAlpha.value)
            .testTag("gift_animation_overlay"),
        contentAlignment = Alignment.Center
    ) {
        // Top Glowing Combo Banner
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (80 + bannerSlide.value.toInt()).dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xEE1E0B36),
                            Color(0xEE6E1152),
                            Color(0xEE092C4E)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(listOf(TikTokCyan, TikTokCoinGold, TikTokRed)),
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activeGift.gift.iconEmoji,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${activeGift.senderName} sent ${activeGift.gift.name}!",
                        color = TikTokWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "to ${activeGift.recipientName}",
                        color = TikTokCyan,
                        fontSize = 12.sp
                    )
                }
                if (activeGift.comboCount > 1) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "x${activeGift.comboCount}",
                        color = TikTokCoinGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Central Giant Gift Animation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scaleAnim.value)
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(activeGift.gift.animationColorHex).copy(alpha = 0.65f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activeGift.gift.iconEmoji,
                    fontSize = 80.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${activeGift.gift.name} • ${activeGift.gift.coins} Coins",
                color = TikTokWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
