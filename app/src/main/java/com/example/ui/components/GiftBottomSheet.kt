package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.VirtualGift
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBottomSheet(
    recipientName: String,
    userCoins: Int,
    onSendGift: (VirtualGift, Int) -> Unit,
    onRechargeCoins: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedGift by remember { mutableStateOf(MockData.virtualGifts.first()) }
    var selectedCombo by remember { mutableIntStateOf(1) }
    var showTelebirrStore by remember { mutableStateOf(false) }

    val comboOptions = listOf(1, 5, 10, 99)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokSheetBg,
        dragHandle = null,
        modifier = modifier.testTag("gift_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 12.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Send Virtual Gift",
                        color = TikTokWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "to $recipientName",
                        color = TikTokGray,
                        fontSize = 12.sp
                    )
                }

                // Coin Balance Pill & Telebirr Recharge Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF262830))
                        .border(1.dp, TelebirrBlue.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .clickable { showTelebirrStore = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("recharge_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = TikTokCoinGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$userCoins",
                        color = TikTokWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(TelebirrBlue)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Recharge 🇪🇹",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Gifts Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                items(MockData.virtualGifts, key = { it.id }) { gift ->
                    val isSelected = selectedGift.id == gift.id
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.05f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "gift_scale"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .scale(scale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) Color(0xFF2D2638) else Color.Transparent
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                brush = if (isSelected) {
                                    Brush.linearGradient(listOf(TikTokCyan, TikTokRed))
                                } else {
                                    Brush.linearGradient(listOf(Color(0xFF2A2C34), Color(0xFF2A2C34)))
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedGift = gift }
                            .padding(vertical = 10.dp, horizontal = 4.dp)
                            .testTag("gift_item_${gift.id}")
                    ) {
                        Text(
                            text = gift.iconEmoji,
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = gift.name,
                            color = TikTokWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = TikTokCoinGold,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${gift.coins}",
                                color = TikTokCoinGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Bottom Action Bar: Multiplier Combos + Send Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E2024))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Combo multipliers
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    comboOptions.forEach { combo ->
                        val isComboSelected = selectedCombo == combo
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isComboSelected) TikTokRed else Color(0xFF2E313C)
                                )
                                .clickable { selectedCombo = combo }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("combo_button_$combo"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "x$combo",
                                color = TikTokWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Total Cost & Send Button
                val totalCost = selectedGift.coins * selectedCombo
                val canAfford = userCoins >= totalCost

                Button(
                    onClick = {
                        if (canAfford) {
                            onSendGift(selectedGift, selectedCombo)
                        } else {
                            showTelebirrStore = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAfford) TikTokRed else TelebirrBlue
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("send_gift_button")
                ) {
                    Text(
                        text = if (canAfford) "Send $totalCost 🪙" else "Recharge with Telebirr",
                        color = TikTokWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    // Telebirr Coin Store Modal
    if (showTelebirrStore) {
        TelebirrCoinStoreModal(
            currentCoins = userCoins,
            onCoinsPurchased = { amount ->
                onRechargeCoins(amount)
            },
            onDismiss = { showTelebirrStore = false }
        )
    }
}
