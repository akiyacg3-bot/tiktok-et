package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LiveStreamManager
import com.example.model.TikTokTab
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite

@Composable
fun TikTokBottomNav(
    selectedTab: TikTokTab,
    onTabSelected: (TikTokTab) -> Unit,
    onGoLiveRequested: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCreateOptionsSheet by remember { mutableStateOf(false) }
    var showAgeVerificationModal by remember { mutableStateOf(false) }
    var showRestrictionDialog by remember { mutableStateOf(false) }

    val userProfile by LiveStreamManager.userProfile.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .drawBehind {
                // Top border: border-t border-zinc-800/50
                drawLine(
                    color = Color(0xFF27272A).copy(alpha = 0.5f),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .navigationBarsPadding()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .testTag("tiktok_bottom_nav")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Home Tab
            BottomNavItem(
                label = "Home",
                filledIcon = Icons.Default.Home,
                outlinedIcon = Icons.Outlined.Home,
                isSelected = selectedTab == TikTokTab.HOME,
                onClick = { onTabSelected(TikTokTab.HOME) },
                testTag = "nav_tab_home"
            )

            // Friends Tab
            BottomNavItem(
                label = "Friends",
                filledIcon = Icons.Default.Group,
                outlinedIcon = Icons.Outlined.Group,
                isSelected = selectedTab == TikTokTab.FRIENDS,
                badge = null,
                onClick = { onTabSelected(TikTokTab.FRIENDS) },
                testTag = "nav_tab_friends"
            )

            // Create (+) TikTok Button
            TikTokCreateButton(
                onClick = { showCreateOptionsSheet = true },
                modifier = Modifier.testTag("nav_tab_create")
            )

            // Inbox Tab
            BottomNavItem(
                label = "Inbox",
                filledIcon = Icons.Default.Mail,
                outlinedIcon = Icons.Outlined.Mail,
                isSelected = selectedTab == TikTokTab.INBOX,
                badge = "3",
                onClick = { onTabSelected(TikTokTab.INBOX) },
                testTag = "nav_tab_inbox"
            )

            // Profile Tab
            BottomNavItem(
                label = "Profile",
                filledIcon = Icons.Default.Person,
                outlinedIcon = Icons.Outlined.Person,
                isSelected = selectedTab == TikTokTab.PROFILE,
                onClick = { onTabSelected(TikTokTab.PROFILE) },
                testTag = "nav_tab_profile"
            )
        }
    }

    // Upload & Create Options Bottom Sheet (Camera vs Go LIVE)
    if (showCreateOptionsSheet) {
        CreateUploadOptionsBottomSheet(
            onDismiss = { showCreateOptionsSheet = false },
            onSelectRecordVideo = {
                showCreateOptionsSheet = false
                onTabSelected(TikTokTab.CREATE)
            },
            onSelectGoLive = {
                showCreateOptionsSheet = false
                // Check age verification & eligibility rules
                if (!userProfile.isAgeVerified) {
                    showAgeVerificationModal = true
                } else if (!userProfile.isEligibleForLive) {
                    // Under 16 restricted
                    Toast.makeText(context, LiveStreamManager.RESTRICTION_MESSAGE, Toast.LENGTH_LONG).show()
                    showRestrictionDialog = true
                } else {
                    // Eligible 16+
                    if (onGoLiveRequested != null) {
                        onGoLiveRequested()
                    } else {
                        onTabSelected(TikTokTab.CREATE)
                    }
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
                if (onGoLiveRequested != null) {
                    onGoLiveRequested()
                } else {
                    onTabSelected(TikTokTab.CREATE)
                }
            }
        )
    }

    // Friendly Under-16 Restriction Dialog
    if (showRestrictionDialog) {
        LiveStreamRestrictionDialog(
            onDismiss = { showRestrictionDialog = false },
            onOpenAgeVerification = {
                showRestrictionDialog = false
                showAgeVerificationModal = true
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUploadOptionsBottomSheet(
    onDismiss: () -> Unit,
    onSelectRecordVideo: () -> Unit,
    onSelectGoLive: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val userProfile by LiveStreamManager.userProfile.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokSheetBg,
        dragHandle = null,
        modifier = Modifier.testTag("upload_options_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Create on TikTok",
                    color = TikTokWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TikTokGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Option 1: Record Video
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF222430))
                    .clickable(onClick = onSelectRecordVideo)
                    .padding(14.dp)
                    .testTag("option_record_video"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TikTokCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = TikTokCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Record Video",
                        color = TikTokWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "15s, 60s, 10m short video with music & filters",
                        color = TikTokGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Option 2: Go LIVE (With Age Restriction Tag & Rules)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF222430))
                    .border(
                        1.dp,
                        if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676).copy(alpha = 0.5f)
                        else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed.copy(alpha = 0.5f)
                        else TikTokRed.copy(alpha = 0.3f),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable(onClick = onSelectGoLive)
                    .padding(14.dp)
                    .testTag("option_go_live"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TikTokRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = null,
                        tint = TikTokRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Go LIVE 🔴",
                            color = TikTokWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676).copy(alpha = 0.2f)
                                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed.copy(alpha = 0.2f)
                                    else Color(0xFF333644)
                                )
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) "16+ Verified ✅"
                                else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) "Under 16 🔒"
                                else "16+ Only 🛡️",
                                color = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676)
                                else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed
                                else TikTokCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) "Start a real-time live broadcast with gifts & chat"
                        else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) "Restricted: Live streaming is only available for 16+"
                        else "Broadcast live stream (Age verification required)",
                        color = if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) Color(0xFFFF8A80) else TikTokGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    filledIcon: ImageVector,
    outlinedIcon: ImageVector,
    isSelected: Boolean,
    badge: String? = null,
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
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = if (isSelected) filledIcon else outlinedIcon,
                contentDescription = label,
                tint = if (isSelected) TikTokWhite else TikTokWhite.copy(alpha = 0.50f),
                modifier = Modifier.size(24.dp)
            )

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .offset(x = 8.dp, y = (-4).dp)
                        .clip(CircleShape)
                        .background(TikTokRed)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            color = if (isSelected) TikTokWhite else TikTokWhite.copy(alpha = 0.50f),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun TikTokCreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 44.dp, height = 30.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Gradient backing (cyan to pink)
        Box(
            modifier = Modifier
                .size(width = 42.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF22D3EE), Color(0xFFEC4899))
                    )
                )
        )

        // Center White pill
        Box(
            modifier = Modifier
                .size(width = 36.dp, height = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Video",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
