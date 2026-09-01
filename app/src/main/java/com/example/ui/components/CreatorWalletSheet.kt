package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.data.MonetizationManager
import com.example.model.CreatorWallet
import com.example.model.GiftTransaction
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorWalletSheet(
    creatorHandle: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val wallets by MonetizationManager.creatorWallets.collectAsState()
    val wallet = wallets[creatorHandle] ?: MonetizationManager.getCreatorWallet(creatorHandle)

    var showWithdrawDialog by remember { mutableStateOf(false) }
    var showApplyDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokSheetBg,
        dragHandle = null,
        modifier = modifier.testTag("creator_wallet_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Top Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(Color(0xFF8A2387), Color(0xFFE94057)))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Creator Monetization",
                                    color = TikTokWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (wallet.isMonetized) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Monetized",
                                        tint = TikTokCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Dashboard for ${wallet.creatorName} (${wallet.creatorHandle})",
                                color = TikTokGray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_creator_wallet_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TikTokWhite.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Monetization Criteria Progress Card
            item {
                CreatorMonetizationCriteriaCard(
                    wallet = wallet,
                    onApplyClick = { showApplyDialog = true },
                    onSimulateBoost = {
                        MonetizationManager.boostCreatorMilestones(wallet.creatorHandle)
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Diamonds & Telebirr Earnings Card
            item {
                CreatorEarningsCard(
                    wallet = wallet,
                    onWithdrawClick = { showWithdrawDialog = true }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Gift Activity Feed Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Viewer Gifts (${wallet.totalGiftsReceived} total)",
                        color = TikTokWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Live Stream & Feed",
                        color = TikTokGray,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // List of Transactions
            if (wallet.transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No gifts received yet. Post videos and start streaming to earn gifts!",
                            color = TikTokGray,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                items(wallet.transactions) { tx ->
                    GiftTransactionItem(tx = tx)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if (showApplyDialog) {
        ApplyMonetizationDialog(
            wallet = wallet,
            onDismiss = { showApplyDialog = false }
        )
    }

    if (showWithdrawDialog) {
        TelebirrWithdrawDialog(
            creatorHandle = creatorHandle,
            accumulatedCoins = wallet.accumulatedCoins,
            onDismiss = { showWithdrawDialog = false }
        )
    }
}

@Composable
private fun CreatorMonetizationCriteriaCard(
    wallet: CreatorWallet,
    onApplyClick: () -> Unit,
    onSimulateBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMonetized = wallet.isMonetized
    val isEligible = wallet.isEligibleToApply

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1B1D26))
            .border(
                1.dp,
                if (isMonetized) Color(0xFF00E676).copy(alpha = 0.6f)
                else if (isEligible) TikTokCyan.copy(alpha = 0.7f)
                else Color(0xFF333644),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .animateContentSize()
            .testTag("creator_monetization_criteria_card")
    ) {
        Column {
            // Header with status tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isMonetized) Icons.Default.Verified else Icons.Default.AutoGraph,
                        contentDescription = null,
                        tint = if (isMonetized) Color(0xFF00E676) else if (isEligible) TikTokCyan else TikTokCoinGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Monetization Criteria",
                        color = TikTokWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isMonetized) Color(0xFF00E676).copy(alpha = 0.15f)
                            else if (isEligible) TikTokCyan.copy(alpha = 0.15f)
                            else Color(0xFFFF9800).copy(alpha = 0.15f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isMonetized) "MONETIZED ✅"
                        else if (isEligible) "CRITERIA MET 🎉"
                        else "IN PROGRESS 🔒",
                        color = if (isMonetized) Color(0xFF00E676)
                        else if (isEligible) TikTokCyan
                        else Color(0xFFFFB74D),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Criteria 1: 5,000 Followers
            val followerMet = wallet.followerCount >= wallet.targetFollowers
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = if (followerMet) Color(0xFF00E676) else TikTokGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Followers Threshold (5,000)",
                            color = TikTokWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "${MockData.formatCount(wallet.followerCount)} / 5,000",
                        color = if (followerMet) Color(0xFF00E676) else TikTokWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { wallet.followerProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (followerMet) Color(0xFF00E676) else TikTokCyan,
                    trackColor = Color(0xFF2C2F3A)
                )

                if (!followerMet) {
                    Text(
                        text = "${5000 - wallet.followerCount} more followers needed",
                        color = TikTokGray,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Criteria 2: 50,000 Total Video Views
            val viewsMet = wallet.totalVideoViews >= wallet.targetViews
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RemoveRedEye,
                            contentDescription = null,
                            tint = if (viewsMet) Color(0xFF00E676) else TikTokGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Total Video Views (50,000)",
                            color = TikTokWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "${MockData.formatCount(wallet.totalVideoViews)} / 50K",
                        color = if (viewsMet) Color(0xFF00E676) else TikTokWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { wallet.viewsProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (viewsMet) Color(0xFF00E676) else TikTokRed,
                    trackColor = Color(0xFF2C2F3A)
                )

                if (!viewsMet) {
                    Text(
                        text = "${MockData.formatCount(50000 - wallet.totalVideoViews)} more views needed across all videos",
                        color = TikTokGray,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Apply for Monetization Action Button
            if (isMonetized) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF00E676).copy(alpha = 0.12f))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Monetization Active! Telebirr cashouts are enabled for your account.",
                        color = Color(0xFFE8F5E9),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Button(
                    onClick = onApplyClick,
                    enabled = isEligible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("apply_monetization_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEligible) TikTokRed else Color(0xFF2D303D),
                        disabledContainerColor = Color(0xFF2D303D)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isEligible) Icons.Default.Stars else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isEligible) Color.White else TikTokGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEligible) "Apply for Monetization ✨" else "Apply for Monetization (Criteria Pending)",
                            color = if (isEligible) Color.White else TikTokGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Demo Boost Helper for testing reaching criteria
                if (!isEligible) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSimulateBoost() }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = TikTokCoinGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Simulate 5K Followers & 50K Views (Demo Boost)",
                            color = TikTokCoinGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreatorEarningsCard(
    wallet: CreatorWallet,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMonetized = wallet.isMonetized
    val estimatedEtb = (wallet.accumulatedCoins * 0.50).toInt()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF1E2028), Color(0xFF2C1930))
                )
            )
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(TikTokCyan.copy(alpha = 0.5f), TikTokRed.copy(alpha = 0.5f))),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Accumulated Balance",
                    color = TikTokGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF3B2D4A))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "1 Diamond = 0.50 ETB",
                        color = TikTokCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${MockData.formatCount(wallet.accumulatedCoins)} Diamonds",
                    color = TikTokWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "≈ $estimatedEtb ETB withdrawable via Telebirr",
                color = Color(0xFF00E676),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Withdraw to Telebirr Action Button
            Button(
                onClick = onWithdrawClick,
                enabled = isMonetized && wallet.accumulatedCoins > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("withdraw_telebirr_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TelebirrBlue,
                    disabledContainerColor = Color(0xFF2D303D)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isMonetized) {
                        Text(
                            text = "tb",
                            color = TelebirrYellow,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Withdraw to Telebirr Account",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = TikTokGray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Telebirr Cashout (Monetization Required)",
                            color = TikTokGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApplyMonetizationDialog(
    wallet: CreatorWallet,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isSubmitting by remember { mutableStateOf(false) }
    var isApproved by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TikTokSheetBg,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = TikTokCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Monetization Application",
                    color = TikTokWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            if (isSubmitting) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = TikTokCyan,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Verifying 5,000 followers and 50,000 video views...",
                            color = TikTokWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            } else if (isApproved) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Congratulations!",
                        color = TikTokWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You are now an officially Monetized Creator! You can now withdraw your Diamonds directly to your Telebirr wallet.",
                        color = TikTokGray,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "You have successfully achieved all creator monetization criteria:",
                        color = TikTokWhite,
                        fontSize = 13.sp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF262830))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "5,000+ Followers (${MockData.formatCount(wallet.followerCount)})",
                                color = TikTokWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "50,000+ Video Views (${MockData.formatCount(wallet.totalVideoViews)})",
                                color = TikTokWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "Click below to complete your application and activate instant Telebirr cashouts.",
                        color = TikTokGray,
                        fontSize = 11.sp
                    )
                }
            }
        },
        confirmButton = {
            if (!isApproved && !isSubmitting) {
                Button(
                    onClick = {
                        scope.launch {
                            isSubmitting = true
                            delay(1200)
                            MonetizationManager.applyForMonetization(wallet.creatorHandle)
                            isSubmitting = false
                            isApproved = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                    modifier = Modifier.testTag("submit_monetization_app_button")
                ) {
                    Text("Submit Application", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else if (isApproved) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676))
                ) {
                    Text("Start Earning", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isSubmitting && !isApproved) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TikTokGray)
                }
            }
        }
    )
}

@Composable
private fun GiftTransactionItem(tx: GiftTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF262830))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("gift_tx_${tx.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = tx.gift.iconEmoji,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tx.senderName,
                        color = TikTokWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "sent ${tx.gift.name} x${tx.comboCount}",
                        color = TikTokGray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = tx.timestamp,
                    color = TikTokGray.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            }
        }

        // Coins / Diamonds Received
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(TikTokCoinGold.copy(alpha = 0.2f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "+${tx.totalCoins} 💎",
                color = TikTokCoinGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TelebirrWithdrawDialog(
    creatorHandle: String,
    accumulatedCoins: Long,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var withdrawPhone by remember { mutableStateOf("0911234567") }
    var isProcessing by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    val etbAmount = (accumulatedCoins * 0.50).toInt()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TikTokSheetBg,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "tb",
                    color = TelebirrYellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Telebirr Cashout",
                    color = TikTokWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            if (isProcessing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = TelebirrYellow,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Processing Telebirr payout of $etbAmount ETB...",
                            color = TikTokWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            } else if (isSuccess) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cashout Transferred!",
                        color = TikTokWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$etbAmount ETB sent to Telebirr $withdrawPhone",
                        color = TikTokGray,
                        fontSize = 12.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Withdraw $accumulatedCoins Diamonds to your Telebirr mobile wallet ($etbAmount ETB):",
                        color = TikTokWhite,
                        fontSize = 13.sp
                    )

                    OutlinedTextField(
                        value = withdrawPhone,
                        onValueChange = { withdrawPhone = it },
                        label = { Text("Telebirr Phone Number") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TikTokWhite,
                            unfocusedTextColor = TikTokWhite,
                            focusedBorderColor = TelebirrBlue,
                            unfocusedBorderColor = Color(0xFF3B3E4A),
                            focusedContainerColor = Color(0xFF1E2028),
                            unfocusedContainerColor = Color(0xFF1E2028)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_phone_input")
                    )
                }
            }
        },
        confirmButton = {
            if (!isSuccess && !isProcessing) {
                Button(
                    onClick = {
                        scope.launch {
                            isProcessing = true
                            delay(1400)
                            MonetizationManager.withdrawCreatorEarnings(
                                creatorHandle = creatorHandle,
                                amountCoins = accumulatedCoins,
                                telebirrPhone = withdrawPhone
                            )
                            isProcessing = false
                            isSuccess = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TelebirrBlue),
                    modifier = Modifier.testTag("confirm_withdraw_button")
                ) {
                    Text("Confirm Withdraw", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else if (isSuccess) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = TelebirrBlue)
                ) {
                    Text("Done", color = Color.White)
                }
            }
        },
        dismissButton = {
            if (!isProcessing && !isSuccess) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TikTokGray)
                }
            }
        }
    )
}
