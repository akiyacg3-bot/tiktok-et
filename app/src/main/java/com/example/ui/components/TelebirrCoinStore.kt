package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.data.MonetizationManager
import com.example.data.TelebirrReceipt
import com.example.model.TelebirrPackage
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Telebirr Official Brand Colors
val TelebirrBlue = Color(0xFF0072CE)
val TelebirrDarkBlue = Color(0xFF003B73)
val TelebirrYellow = Color(0xFFFDB813)
val TelebirrLightBg = Color(0xFF0D253A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelebirrCoinStoreModal(
    currentCoins: Int,
    onCoinsPurchased: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedPackage by remember { mutableStateOf(MockData.telebirrPackages[1]) } // 500 Coins default
    var telebirrPhone by remember { mutableStateOf("0911234567") }
    var isProcessing by remember { mutableStateOf(false) }
    var processingStep by remember { mutableStateOf("") }
    var paymentReceipt by remember { mutableStateOf<TelebirrReceipt?>(null) }

    fun processPayment() {
        scope.launch {
            isProcessing = true
            processingStep = "Connecting to Telebirr Gateway..."
            delay(900)
            processingStep = "Sending USSD Push to ${telebirrPhone}..."
            delay(1100)
            processingStep = "Verifying PIN & Transferring ${selectedPackage.priceEtb} ETB..."
            delay(900)

            val receipt = MonetizationManager.rechargeCoinsWithTelebirr(
                pkg = selectedPackage,
                phoneNumber = telebirrPhone
            )
            onCoinsPurchased(selectedPackage.coins)
            paymentReceipt = receipt
            isProcessing = false
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokSheetBg,
        dragHandle = null,
        modifier = modifier.testTag("telebirr_coin_store_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Top Telebirr Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Telebirr Logo Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(TelebirrBlue, TelebirrYellow))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "tb",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "telebirr Coin Store",
                                color = TikTokWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(TelebirrBlue.copy(alpha = 0.3f))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "OFFICIAL",
                                    color = TelebirrYellow,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Instant mobile payment with Ethio Telecom",
                            color = TikTokGray,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_telebirr_store_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TikTokWhite.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (paymentReceipt != null) {
                // Payment Success Screen
                TelebirrSuccessView(
                    receipt = paymentReceipt!!,
                    onDone = onDismiss
                )
            } else if (isProcessing) {
                // Payment Processing Screen
                TelebirrProcessingView(
                    stepText = processingStep,
                    amountEtb = selectedPackage.priceEtb,
                    coins = selectedPackage.coins
                )
            } else {
                // Coin Packages Selection Screen
                // Balance Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TelebirrLightBg)
                        .border(1.dp, TelebirrBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = TikTokCoinGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Current Balance",
                            color = TikTokWhite.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "$currentCoins Coins",
                        color = TikTokCoinGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Select Coin Package:",
                    color = TikTokWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Packages List
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MockData.telebirrPackages.forEach { pkg ->
                        val isSelected = selectedPackage.id == pkg.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) TelebirrBlue.copy(alpha = 0.25f) else Color(0xFF262830)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) TelebirrBlue else Color(0xFF3B3E4A),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedPackage = pkg }
                                .padding(horizontal = 14.dp, vertical = 11.dp)
                                .testTag("telebirr_package_${pkg.coins}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = TikTokCoinGold,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${pkg.coins} Coins",
                                            color = TikTokWhite,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (pkg.isPopular) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(TikTokRed)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "POPULAR",
                                                    color = Color.White,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                    if (pkg.bonusLabel.isNotBlank()) {
                                        Text(
                                            text = pkg.bonusLabel,
                                            color = TelebirrYellow,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Price in ETB badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        if (isSelected) TelebirrBlue else Color(0xFF353945)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${pkg.priceEtb} ETB",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Telebirr Mobile Number Input
                Text(
                    text = "Telebirr Account (Phone Number):",
                    color = TikTokWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = telebirrPhone,
                    onValueChange = { telebirrPhone = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("telebirr_phone_input"),
                    placeholder = { Text("09XXXXXXXX or 07XXXXXXXX", color = TikTokGray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = TelebirrBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    },
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
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Action: Pay with Telebirr Button
                Button(
                    onClick = { processPayment() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("pay_with_telebirr_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TelebirrBlue
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "tb",
                            color = TelebirrYellow,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pay ${selectedPackage.priceEtb} ETB with Telebirr",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = TikTokGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Encrypted & 100% secure with Ethio Telecom Telebirr",
                        color = TikTokGray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TelebirrProcessingView(
    stepText: String,
    amountEtb: Int,
    coins: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = TelebirrYellow,
                strokeWidth = 3.5.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Telebirr Payment in Progress",
                color = TikTokWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stepText,
                color = TelebirrBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Recharging $coins Coins for $amountEtb ETB",
                color = TikTokGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TelebirrSuccessView(
    receipt: TelebirrReceipt,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFF00C853).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF00E676),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Payment Successful! 🎉",
            color = TikTokWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "+${receipt.coinsPurchased} Coins added to your balance",
            color = TikTokCoinGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Receipt Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF262830))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReceiptRow(label = "Transaction ID", value = receipt.transactionId)
            ReceiptRow(label = "Amount Paid", value = "${receipt.amountEtb} ETB")
            ReceiptRow(label = "Coins Received", value = "+${receipt.coinsPurchased} Coins")
            ReceiptRow(label = "Telebirr Account", value = receipt.phoneNumber)
            ReceiptRow(label = "Timestamp", value = receipt.timestamp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("telebirr_success_done_button"),
            colors = ButtonDefaults.buttonColors(containerColor = TelebirrBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Back to Gifting",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TikTokGray, fontSize = 12.sp)
        Text(text = value, color = TikTokWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
