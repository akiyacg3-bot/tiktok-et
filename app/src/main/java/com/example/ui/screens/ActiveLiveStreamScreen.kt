package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LiveChatMessage
import com.example.data.LiveStreamManager
import com.example.data.MockData
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ActiveLiveStreamScreen(
    onEndLive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val liveState by LiveStreamManager.liveStreamState.collectAsState()
    val userProfile by LiveStreamManager.userProfile.collectAsState()

    var streamDurationSeconds by remember { mutableLongStateOf(0L) }
    var showEndConfirmDialog by remember { mutableStateOf(false) }
    var showSummaryDialog by remember { mutableStateOf(false) }
    var chatInputText by remember { mutableStateOf("") }
    var isMuted by remember { mutableStateOf(false) }
    var isBeautyActive by remember { mutableStateOf(true) }

    val listState = rememberLazyListState()

    // Timer loop for Live Stream duration
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            streamDurationSeconds++
        }
    }

    // Auto-scroll chat to latest message
    LaunchedEffect(liveState.messages.size) {
        if (liveState.messages.isNotEmpty()) {
            listState.animateScrollToItem(liveState.messages.size - 1)
        }
    }

    // Periodic simulated viewer joins / comments
    LaunchedEffect(Unit) {
        val sampleComments = listOf(
            "Greetings from Bole! 👋",
            "This stream is fire 🔥🔥",
            "Love the music choice! 🎵",
            "Can you do a shoutout to Addis? 🇪🇹",
            "Keep it up! Amazing content",
            "Sending love from Hawassa! ❤️"
        )
        val viewers = listOf("Abebe K.", "Tigist G.", "Blen T.", "Natnael W.", "Helina M.", "Ermias S.")

        while (true) {
            delay(4000)
            val randomViewer = viewers.random()
            val randomComment = sampleComments.random()
            LiveStreamManager.sendLiveMessage(randomViewer, randomComment)
            LiveStreamManager.addLiveLike()
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val livePulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("active_live_stream_screen")
    ) {
        // 1. Top Header Bar: Host info, LIVE badge, Viewers, Diamonds, End Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Host Pill (Avatar + Name + Viewers)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                // Host Avatar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(TikTokCyan, TikTokRed))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AR",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Alex Rivera (Host)",
                            color = TikTokWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(TikTokCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "16+ ✅",
                                color = TikTokCyan,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val mins = streamDurationSeconds / 60
                    val secs = streamDurationSeconds % 60
                    Text(
                        text = String.format("%02d:%02d • %s", mins, secs, liveState.category),
                        color = TikTokGray,
                        fontSize = 10.sp
                    )
                }
            }

            // Right side stats: Viewers & Diamonds & End Live Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Viewers pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = TikTokWhite,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${liveState.viewerCount}",
                        color = TikTokWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Diamonds pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF3B2D4A).copy(alpha = 0.8f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = TikTokCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${liveState.diamondsEarned}",
                        color = TikTokCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // End Live Button (X)
                IconButton(
                    onClick = { showEndConfirmDialog = true },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .testTag("end_live_stream_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "End Live",
                        tint = TikTokWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Live Title Banner (Subtle overlay near top)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 58.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(livePulse)
                        .clip(CircleShape)
                        .background(TikTokRed)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = liveState.title,
                    color = TikTokWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 2. Chat Feed Overlay (Bottom Left)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, end = 80.dp, bottom = 64.dp)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(liveState.messages) { msg ->
                    LiveChatMessageBubble(msg = msg)
                }
            }
        }

        // 3. Right-Side Interactive Actions (Likes, Gifts, Tools)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Flip Camera
            LiveToolAction(
                icon = Icons.Default.FlipCameraAndroid,
                label = "Flip",
                onClick = { Toast.makeText(context, "Camera Flipped", Toast.LENGTH_SHORT).show() }
            )

            // Mute / Unmute Mic
            LiveToolAction(
                icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                label = if (isMuted) "Unmute" else "Mute",
                iconTint = if (isMuted) TikTokRed else TikTokWhite,
                onClick = {
                    isMuted = !isMuted
                    val msg = if (isMuted) "Microphone Muted" else "Microphone Active"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            )

            // Beauty Filter
            LiveToolAction(
                icon = Icons.Default.AutoFixHigh,
                label = "Beauty",
                iconTint = if (isBeautyActive) TikTokCyan else TikTokWhite,
                onClick = {
                    isBeautyActive = !isBeautyActive
                    Toast.makeText(context, "Beauty filter toggled", Toast.LENGTH_SHORT).show()
                }
            )

            // Simulate Viewer Gift Trigger (For interactive testing)
            LiveToolAction(
                icon = Icons.Default.CardGiftcard,
                label = "Gift",
                iconTint = TikTokCoinGold,
                onClick = {
                    val gift = MockData.virtualGifts.random()
                    LiveStreamManager.receiveViewerGift(gift, (1..3).random())
                    Toast.makeText(context, "Viewer sent ${gift.name} ${gift.iconEmoji}!", Toast.LENGTH_SHORT).show()
                }
            )

            // Heart / Like Tap Trigger
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(TikTokRed)
                    .clickable {
                        LiveStreamManager.addLiveLike()
                    }
                    .testTag("live_like_action_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "${liveState.likesCount}",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 4. Bottom Chat Input Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatInputText,
                onValueChange = { chatInputText = it },
                placeholder = { Text("Comment to your viewers...", color = TikTokGray, fontSize = 12.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (chatInputText.isNotBlank()) {
                            LiveStreamManager.sendLiveMessage("Alex Rivera (Host)", chatInputText.trim())
                            chatInputText = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TikTokWhite,
                    unfocusedTextColor = TikTokWhite,
                    focusedBorderColor = TikTokCyan,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Black.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    if (chatInputText.isNotBlank()) {
                        IconButton(
                            onClick = {
                                LiveStreamManager.sendLiveMessage("Alex Rivera (Host)", chatInputText.trim())
                                chatInputText = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = TikTokCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("live_chat_input")
            )
        }
    }

    // Confirmation Dialog before ending Live Stream
    if (showEndConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showEndConfirmDialog = false },
            containerColor = TikTokSheetBg,
            title = {
                Text(
                    text = "End Live Stream?",
                    color = TikTokWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to end your current Live Stream session?",
                    color = TikTokGray,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEndConfirmDialog = false
                        LiveStreamManager.endLiveSession()
                        showSummaryDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                    modifier = Modifier.testTag("confirm_end_live_button")
                ) {
                    Text("End Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndConfirmDialog = false }) {
                    Text("Cancel", color = TikTokGray)
                }
            }
        )
    }

    // Live Stream Summary / Report Dialog
    if (showSummaryDialog) {
        LiveStreamSummaryDialog(
            durationSeconds = streamDurationSeconds,
            viewersReached = liveState.viewerCount + 450,
            diamondsEarned = liveState.diamondsEarned,
            totalLikes = liveState.likesCount,
            onClose = onEndLive
        )
    }
}

@Composable
private fun LiveChatMessageBubble(msg: LiveChatMessage) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (msg.isSystem) Color(0xFF1E2640).copy(alpha = 0.85f)
                else Color.Black.copy(alpha = 0.45f)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (msg.badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(TikTokRed.copy(alpha = 0.8f))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = msg.badge,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = "${msg.senderName}: ",
                color = if (msg.isSystem) TikTokCyan else TikTokGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = msg.message,
                color = TikTokWhite,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun LiveToolAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    iconTint: Color = TikTokWhite,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TikTokWhite,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LiveStreamSummaryDialog(
    durationSeconds: Long,
    viewersReached: Int,
    diamondsEarned: Long,
    totalLikes: Long,
    onClose: () -> Unit
) {
    val mins = durationSeconds / 60
    val secs = durationSeconds % 60
    val estimatedEtb = (diamondsEarned * 0.50).toInt()

    AlertDialog(
        onDismissRequest = onClose,
        containerColor = TikTokSheetBg,
        modifier = Modifier.testTag("live_summary_dialog"),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 LIVE Ended",
                    color = TikTokWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Stream Broadcast Summary",
                    color = TikTokGray,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 2x2 Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatSummaryCard(
                        title = "Live Duration",
                        value = String.format("%02d:%02d", mins, secs),
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        title = "Total Viewers",
                        value = MockData.formatCount(viewersReached),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatSummaryCard(
                        title = "Diamonds Earned",
                        value = "$diamondsEarned 💎",
                        subtitle = "≈ $estimatedEtb ETB",
                        valueColor = TikTokCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatSummaryCard(
                        title = "Total Likes",
                        value = MockData.formatCount(totalLikes),
                        valueColor = TikTokRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("close_summary_button")
            ) {
                Text("Done", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun StatSummaryCard(
    title: String,
    value: String,
    subtitle: String? = null,
    valueColor: Color = TikTokWhite,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF232530))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = TikTokGray,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = valueColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = Color(0xFF00E676),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
