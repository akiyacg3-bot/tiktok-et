package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LiveStreamManager
import com.example.data.MockData
import com.example.data.MonetizationManager
import com.example.ui.components.AgeVerificationDialog
import com.example.ui.components.CreatorWalletSheet
import com.example.ui.components.LiveStreamRestrictionDialog
import com.example.ui.components.TelebirrBlue
import com.example.ui.components.TelebirrYellow
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreatorWalletSheet by remember { mutableStateOf(false) }
    val currentHandle = "@arivera_official"

    val wallets by MonetizationManager.creatorWallets.collectAsState()
    val wallet = wallets[currentHandle] ?: MonetizationManager.getCreatorWallet(currentHandle)

    val tabIcons = listOf(Icons.Default.GridOn, Icons.Default.Lock, Icons.Default.Bookmark, Icons.Default.Favorite)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .statusBarsPadding()
            .testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("profile_back_button")) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TikTokWhite
                )
            }

            Text(
                text = "Alex Rivera",
                color = TikTokWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { showCreatorWalletSheet = true },
                modifier = Modifier.testTag("profile_wallet_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Creator Wallet",
                    tint = TikTokCoinGold
                )
            }
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(96.dp)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(listOf(TikTokCyan, TikTokRed)),
                    shape = CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "A",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "@arivera_official",
            color = TikTokWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Creator Earnings & Monetization Quick Banner
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E2028))
                .border(
                    1.dp,
                    if (wallet.isMonetized) Color(0xFF00E676).copy(alpha = 0.5f)
                    else if (wallet.isEligibleToApply) TikTokCyan.copy(alpha = 0.5f)
                    else Color(0xFF333644),
                    RoundedCornerShape(12.dp)
                )
                .clickable { showCreatorWalletSheet = true }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("creator_earnings_banner"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (wallet.isMonetized) Icons.Default.Diamond else Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = if (wallet.isMonetized) Color(0xFF00E5FF) else if (wallet.isEligibleToApply) TikTokCyan else TikTokCoinGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (wallet.isMonetized) "Creator Monetization" else "Monetization Criteria",
                            color = TikTokWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (wallet.isMonetized) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Text(
                        text = if (wallet.isMonetized) "${MockData.formatCount(wallet.accumulatedCoins)} 💎 • Telebirr Ready"
                        else if (wallet.isEligibleToApply) "Criteria Met! Ready to Apply ✨"
                        else "${MockData.formatCount(wallet.followerCount)}/5K followers • ${MockData.formatCount(wallet.totalVideoViews)}/50K views",
                        color = if (wallet.isEligibleToApply && !wallet.isMonetized) TikTokCyan else TikTokGray,
                        fontSize = 11.sp,
                        fontWeight = if (wallet.isEligibleToApply && !wallet.isMonetized) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (wallet.isMonetized) TelebirrBlue
                        else if (wallet.isEligibleToApply) TikTokRed
                        else Color(0xFF2C2F3A)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (wallet.isMonetized) "Telebirr Cashout >"
                    else if (wallet.isEligibleToApply) "Apply Now >"
                    else "Dashboard >",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Live Streaming (16+) Age Verification Quick Status
        val userProfile by LiveStreamManager.userProfile.collectAsState()
        var showAgeVerificationModal by remember { mutableStateOf(false) }
        var showRestrictionDialog by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF181A22))
                .border(
                    1.dp,
                    if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676).copy(alpha = 0.4f)
                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed.copy(alpha = 0.4f)
                    else TikTokCyan.copy(alpha = 0.3f),
                    RoundedCornerShape(12.dp)
                )
                .clickable {
                    if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) {
                        showRestrictionDialog = true
                    } else {
                        showAgeVerificationModal = true
                    }
                }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("profile_live_age_banner"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Icons.Default.Videocam
                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) Icons.Default.Lock
                    else Icons.Default.Security,
                    contentDescription = null,
                    tint = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) Color(0xFF00E676)
                    else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) TikTokRed
                    else TikTokCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "LIVE Streaming Eligibility (16+)",
                        color = TikTokWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (userProfile.isAgeVerified && userProfile.isEligibleForLive) "Verified: Age ${userProfile.calculatedAge} yrs (Eligible to Broadcast)"
                        else if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) "Restricted: Age ${userProfile.calculatedAge} yrs (Requires 16+)"
                        else "Not verified yet • Tap to verify birthdate",
                        color = if (userProfile.isAgeVerified && !userProfile.isEligibleForLive) Color(0xFFFF8A80) else TikTokGray,
                        fontSize = 10.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF262834))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (userProfile.isAgeVerified) "Edit >" else "Verify >",
                    color = TikTokCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showAgeVerificationModal) {
            AgeVerificationDialog(
                onDismiss = { showAgeVerificationModal = false },
                onVerifiedSuccess = { showAgeVerificationModal = false }
            )
        }

        if (showRestrictionDialog) {
            LiveStreamRestrictionDialog(
                onDismiss = { showRestrictionDialog = false },
                onOpenAgeVerification = {
                    showRestrictionDialog = false
                    showAgeVerificationModal = true
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Stats Row: Following | Followers | Likes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStatItem(count = "284", label = "Following")
            ProfileStatItem(count = "142.8K", label = "Followers")
            ProfileStatItem(count = "1.9M", label = "Likes")
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons: Edit Profile & Share Profile
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262830)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("edit_profile_button")
            ) {
                Text(text = "Edit profile", color = TikTokWhite, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showCreatorWalletSheet = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF262830)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .testTag("share_profile_button")
            ) {
                Text(text = "Creator Wallet", color = TikTokWhite, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bio
        Text(
            text = "🎬 Visual Creator & Creative Developer\n✨ Exploring short video magic & animation\n📍 Los Angeles, CA",
            color = TikTokWhite.copy(alpha = 0.88f),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Video Grid Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = TikTokBlack,
            contentColor = TikTokWhite,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = TikTokWhite,
                    height = 2.dp
                )
            }
        ) {
            tabIcons.forEachIndexed { index, icon ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selectedTab == index) TikTokWhite else TikTokGray
                        )
                    }
                )
            }
        }

        // 3-Column Video Thumbnail Grid
        val mockGridItems = listOf("1.4M", "820K", "540K", "1.1M", "310K", "980K")
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(mockGridItems) { views ->
                Box(
                    modifier = Modifier
                        .aspectRatio(0.75f)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1E2028), Color(0xFF2A2C38))
                            )
                        )
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomStart),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = TikTokWhite,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = views,
                            color = TikTokWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showCreatorWalletSheet) {
        CreatorWalletSheet(
            creatorHandle = currentHandle,
            onDismiss = { showCreatorWalletSheet = false }
        )
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = TikTokWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = TikTokGray,
            fontSize = 12.sp
        )
    }
}
