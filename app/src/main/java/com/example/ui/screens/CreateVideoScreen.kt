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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LiveStreamManager
import com.example.ui.components.AgeVerificationDialog
import com.example.ui.components.LiveStreamRestrictionDialog
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite

enum class CameraRecordMode {
    FIFTEEN_S,
    SIXTY_S,
    TEN_M,
    PHOTO,
    LIVE
}

@Composable
fun CreateVideoScreen(
    onClose: () -> Unit,
    initialMode: CameraRecordMode = CameraRecordMode.FIFTEEN_S,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMode by remember { mutableStateOf(initialMode) }
    var isRecording by remember { mutableStateOf(false) }

    // Live Stream Setup State
    val userProfile by LiveStreamManager.userProfile.collectAsState()
    val liveState by LiveStreamManager.liveStreamState.collectAsState()

    var liveStreamTitle by remember { mutableStateOf("✨ Hanging out & Chilling with Addis Vibes!") }
    var selectedLiveTopic by remember { mutableStateOf("Chat & Chill") }
    val liveTopics = listOf("Chat & Chill", "Music & Dance", "Gaming", "Ethiopian Culture", "Tech & Coding", "Q&A")

    var showAgeVerificationModal by remember { mutableStateOf(false) }
    var showRestrictionModal by remember { mutableStateOf(false) }
    var isBroadcastingLive by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "rec_pulse")
    val recPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    if (isBroadcastingLive) {
        ActiveLiveStreamScreen(
            onEndLive = {
                isBroadcastingLive = false
                onClose()
            }
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    if (selectedMode == CameraRecordMode.LIVE) {
                        listOf(Color(0xFF1B1624), Color(0xFF261C30), Color(0xFF141926))
                    } else {
                        listOf(Color(0xFF141E30), Color(0xFF243B55))
                    }
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("create_video_screen")
    ) {
        // Top Bar: Close Button, Add Sound / Live Title Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose, modifier = Modifier.testTag("close_camera_button")) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Camera",
                    tint = TikTokWhite
                )
            }

            if (selectedMode == CameraRecordMode.LIVE) {
                // Live Stream Title Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TikTokRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE Studio (16+)",
                        color = TikTokWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Add Sound pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.45f))
                        .clickable {
                            Toast.makeText(context, "Sound Picker Opened", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = TikTokCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add sound",
                        color = TikTokWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.size(48.dp))
        }

        // LIVE MODE SETUP UI
        if (selectedMode == CameraRecordMode.LIVE) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 70.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Stream Title input
                OutlinedTextField(
                    value = liveStreamTitle,
                    onValueChange = { liveStreamTitle = it },
                    label = { Text("LIVE Stream Title", fontSize = 12.sp) },
                    placeholder = { Text("What is your LIVE stream about?", color = TikTokGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TikTokWhite,
                        unfocusedTextColor = TikTokWhite,
                        focusedBorderColor = TikTokCyan,
                        unfocusedBorderColor = Color(0xFF3B3E4A),
                        focusedContainerColor = Color.Black.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("live_title_input")
                )

                // Category / Topic Selection
                Text(
                    text = "Select Stream Topic",
                    color = TikTokWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(liveTopics) { topic ->
                        val isSelected = selectedLiveTopic == topic
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) TikTokCyan else Color.Black.copy(alpha = 0.5f))
                                .border(
                                    1.dp,
                                    if (isSelected) TikTokCyan else Color(0xFF3B3E4A),
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedLiveTopic = topic }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = topic,
                                color = if (isSelected) Color.Black else TikTokWhite,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Age Verification & Eligibility Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(
                            1.dp,
                            if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676).copy(alpha = 0.6f)
                            else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed.copy(alpha = 0.6f)
                            else TikTokCyan.copy(alpha = 0.4f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showAgeVerificationModal = true }
                        .padding(12.dp)
                        .testTag("live_age_eligibility_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Icons.Default.CheckCircle
                                else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) Icons.Default.Lock
                                else Icons.Default.Security,
                                contentDescription = null,
                                tint = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676)
                                else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed
                                else TikTokCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) "Age Verified (16+) ✅"
                                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) "Under 16 Restricted 🔒"
                                    else "Age Verification Required (16+)",
                                    color = TikTokWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) "Creator age: ${userProfile.calculatedAge} yrs • Eligible to Go LIVE"
                                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) "Age: ${userProfile.calculatedAge} yrs • Live streaming only for 16+"
                                    else "Tap to verify your birthdate before going LIVE",
                                    color = if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) Color(0xFFFF8A80) else TikTokGray,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Text(
                            text = "Verify >",
                            color = TikTokCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Right side camera controls (Flip, Speed, Beauty, Timer, Flash)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CameraToolButton(icon = Icons.Default.FlipCameraAndroid, label = "Flip") {
                Toast.makeText(context, "Camera Flipped", Toast.LENGTH_SHORT).show()
            }
            if (selectedMode != CameraRecordMode.LIVE) {
                CameraToolButton(icon = Icons.Default.Speed, label = "Speed") {}
                CameraToolButton(icon = Icons.Default.AutoFixHigh, label = "Beauty") {}
                CameraToolButton(icon = Icons.Default.Timer, label = "Timer") {}
                CameraToolButton(icon = Icons.Default.FlashOn, label = "Flash") {}
            } else {
                CameraToolButton(icon = Icons.Default.AutoFixHigh, label = "Beauty") {
                    Toast.makeText(context, "Beauty Enhanced", Toast.LENGTH_SHORT).show()
                }
                CameraToolButton(icon = Icons.Default.FlashOn, label = "Light") {}
            }
        }

        // Bottom Controls: Mode Selector & Action Button (Record / Go LIVE)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode selector (15s, 60s, 10m, Photo, LIVE)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                item {
                    ModeSelectorItem(
                        title = "15s",
                        isSelected = selectedMode == CameraRecordMode.FIFTEEN_S,
                        onClick = { selectedMode = CameraRecordMode.FIFTEEN_S }
                    )
                }
                item {
                    ModeSelectorItem(
                        title = "60s",
                        isSelected = selectedMode == CameraRecordMode.SIXTY_S,
                        onClick = { selectedMode = CameraRecordMode.SIXTY_S }
                    )
                }
                item {
                    ModeSelectorItem(
                        title = "10m",
                        isSelected = selectedMode == CameraRecordMode.TEN_M,
                        onClick = { selectedMode = CameraRecordMode.TEN_M }
                    )
                }
                item {
                    ModeSelectorItem(
                        title = "Photo",
                        isSelected = selectedMode == CameraRecordMode.PHOTO,
                        onClick = { selectedMode = CameraRecordMode.PHOTO }
                    )
                }
                item {
                    ModeSelectorItem(
                        title = "LIVE 🔴",
                        isSelected = selectedMode == CameraRecordMode.LIVE,
                        highlightColor = TikTokRed,
                        onClick = { selectedMode = CameraRecordMode.LIVE }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (selectedMode == CameraRecordMode.LIVE) {
                // Big "Go LIVE" Button
                Button(
                    onClick = {
                        // Check age verification and eligibility
                        if (!userProfile.isAgeVerified) {
                            showAgeVerificationModal = true
                        } else if (!userProfile.isEligibleForLive) {
                            // User is under 16 - show restriction notification & modal
                            Toast.makeText(context, LiveStreamManager.RESTRICTION_MESSAGE, Toast.LENGTH_LONG).show()
                            showRestrictionModal = true
                        } else {
                            // User is 16+ and eligible - Start Live Stream
                            val success = LiveStreamManager.startLiveSession(liveStreamTitle, selectedLiveTopic)
                            if (success) {
                                isBroadcastingLive = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TikTokRed
                    ),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(52.dp)
                        .testTag("go_live_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Go LIVE 🔴",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Main Big Record Button for Normal Videos
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .scale(if (isRecording) recPulse else 1f)
                        .clickable {
                            isRecording = !isRecording
                            val msg = if (isRecording) "Recording started... 🔴" else "Video captured! 🎉"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                        .testTag("record_video_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isRecording) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White)
                        )
                    }
                }
            }
        }
    }

    // Age Verification Dialog
    if (showAgeVerificationModal) {
        AgeVerificationDialog(
            onDismiss = { showAgeVerificationModal = false },
            onVerifiedSuccess = {
                showAgeVerificationModal = false
                val success = LiveStreamManager.startLiveSession(liveStreamTitle, selectedLiveTopic)
                if (success) {
                    isBroadcastingLive = true
                }
            }
        )
    }

    // Friendly Restriction Notification Dialog
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

@Composable
private fun ModeSelectorItem(
    title: String,
    isSelected: Boolean,
    highlightColor: Color = TikTokWhite,
    onClick: () -> Unit
) {
    Text(
        text = title,
        color = if (isSelected) highlightColor else TikTokGray,
        fontSize = 14.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Composable
fun CameraToolButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = TikTokWhite,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = TikTokWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
