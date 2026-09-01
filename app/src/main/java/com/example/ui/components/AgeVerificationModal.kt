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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LiveStreamManager
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkBg
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeVerificationDialog(
    onDismiss: () -> Unit,
    onVerifiedSuccess: () -> Unit
) {
    val currentProfile by LiveStreamManager.userProfile.collectAsState()

    var selectedYear by remember { mutableIntStateOf(currentProfile.birthYear) }
    var selectedMonth by remember { mutableIntStateOf(currentProfile.birthMonth) }
    var selectedDay by remember { mutableIntStateOf(currentProfile.birthDay) }

    // Dropdown expansion states
    var expandedYear by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }

    val calculatedAge = remember(selectedYear, selectedMonth, selectedDay) {
        val currentYear = 2026
        val currentMonth = 9
        val currentDay = 1
        var age = currentYear - selectedYear
        if (currentMonth < selectedMonth || (currentMonth == selectedMonth && currentDay < selectedDay)) {
            age--
        }
        age
    }

    val isEligible = calculatedAge >= 16

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TikTokSheetBg,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("age_verification_modal"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(TikTokCyan, TikTokRed)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Age Verification",
                            color = TikTokWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Live Stream Eligibility (16+)",
                            color = TikTokCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_age_verification_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TikTokGray
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Info Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF242630))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = TikTokCyan,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "To keep our community safe, TikTok requires creators to be at least 16 years old to start a Live Stream.",
                            color = TikTokWhite.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Date of Birth Section
                Text(
                    text = "Confirm Your Date of Birth",
                    color = TikTokWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Selectors for Year and Month
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Year Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedYear,
                        onExpandedChange = { expandedYear = !expandedYear },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = "$selectedYear",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Year", fontSize = 11.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TikTokWhite,
                                unfocusedTextColor = TikTokWhite,
                                focusedBorderColor = TikTokCyan,
                                unfocusedBorderColor = Color(0xFF3B3E4A),
                                focusedContainerColor = Color(0xFF1E2028),
                                unfocusedContainerColor = Color(0xFF1E2028)
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("birth_year_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = expandedYear,
                            onDismissRequest = { expandedYear = false },
                            modifier = Modifier.background(Color(0xFF262830))
                        ) {
                            val years = (1970..2020).reversed().toList()
                            years.take(30).forEach { year ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "$year ${if (2026 - year < 16) "(Under 16)" else ""}",
                                            color = if (2026 - year < 16) Color(0xFFFF8A80) else TikTokWhite
                                        )
                                    },
                                    onClick = {
                                        selectedYear = year
                                        expandedYear = false
                                    }
                                )
                            }
                        }
                    }

                    // Month Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedMonth,
                        onExpandedChange = { expandedMonth = !expandedMonth },
                        modifier = Modifier.weight(1f)
                    ) {
                        val monthNames = listOf(
                            "Jan (01)", "Feb (02)", "Mar (03)", "Apr (04)",
                            "May (05)", "Jun (06)", "Jul (07)", "Aug (08)",
                            "Sep (09)", "Oct (10)", "Nov (11)", "Dec (12)"
                        )
                        OutlinedTextField(
                            value = monthNames.getOrElse(selectedMonth - 1) { "Jan (01)" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Month", fontSize = 11.sp) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TikTokWhite,
                                unfocusedTextColor = TikTokWhite,
                                focusedBorderColor = TikTokCyan,
                                unfocusedBorderColor = Color(0xFF3B3E4A),
                                focusedContainerColor = Color(0xFF1E2028),
                                unfocusedContainerColor = Color(0xFF1E2028)
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("birth_month_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMonth,
                            onDismissRequest = { expandedMonth = false },
                            modifier = Modifier.background(Color(0xFF262830))
                        ) {
                            monthNames.forEachIndexed { index, monthName ->
                                DropdownMenuItem(
                                    text = { Text(monthName, color = TikTokWhite) },
                                    onClick = {
                                        selectedMonth = index + 1
                                        expandedMonth = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Quick Demo Presets for Testing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isEligible) TikTokCyan.copy(alpha = 0.2f) else Color(0xFF262830))
                            .border(
                                1.dp,
                                if (isEligible) TikTokCyan else Color(0xFF3B3E4A),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedYear = 2004
                                selectedMonth = 5
                                selectedDay = 15
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Test 22 yrs (Eligible)",
                            color = if (isEligible) TikTokCyan else TikTokWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isEligible) TikTokRed.copy(alpha = 0.2f) else Color(0xFF262830))
                            .border(
                                1.dp,
                                if (!isEligible) TikTokRed else Color(0xFF3B3E4A),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedYear = 2012
                                selectedMonth = 5
                                selectedDay = 15
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Test 14 yrs (Under 16)",
                            color = if (!isEligible) TikTokRed else TikTokWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Age Calculation & Status Banner
                if (isEligible) {
                    // Eligible State (16+)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF00E676).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFF00E676).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                            .testTag("eligible_status_banner")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Age: $calculatedAge years old (Eligible ✅)",
                                    color = Color(0xFF00E676),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "You meet the age requirement to broadcast Live Streams.",
                                    color = TikTokWhite.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                } else {
                    // Ineligible State (<16) - Prominently display the exact restriction message
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(TikTokRed.copy(alpha = 0.15f))
                            .border(1.dp, TikTokRed.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                            .testTag("restricted_status_banner")
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = TikTokRed,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Age: $calculatedAge years old (Restricted 🔒)",
                                    color = TikTokRed,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = LiveStreamManager.RESTRICTION_MESSAGE,
                                    color = Color(0xFFFFCDD2),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val eligible = LiveStreamManager.verifyAge(selectedYear, selectedMonth, selectedDay)
                    if (eligible) {
                        onVerifiedSuccess()
                    }
                },
                enabled = isEligible,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TikTokRed,
                    disabledContainerColor = Color(0xFF2D303D)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("confirm_age_verification_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isEligible) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Confirm & Go LIVE",
                            color = Color.White,
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
                            text = "Under 16 (Restricted)",
                            color = TikTokGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // Update state even if cancelled so selection is reflected in profile/tests
                    LiveStreamManager.updateBirthDate(selectedYear, selectedMonth, selectedDay)
                    onDismiss()
                }
            ) {
                Text("Cancel", color = TikTokGray)
            }
        }
    )
}

@Composable
fun LiveStreamRestrictionDialog(
    onDismiss: () -> Unit,
    onOpenAgeVerification: () -> Unit
) {
    val currentProfile by LiveStreamManager.userProfile.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TikTokSheetBg,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("live_restriction_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = TikTokRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Live Streaming Restricted",
                    color = TikTokWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(TikTokRed.copy(alpha = 0.12f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = LiveStreamManager.RESTRICTION_MESSAGE,
                        color = Color(0xFFFFEBEE),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp
                    )
                }

                Text(
                    text = "Current registered age: ${currentProfile.calculatedAge} years old. Live broadcasting requires creator age verification of 16 years or older.",
                    color = TikTokGray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onOpenAgeVerification()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TikTokCyan),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("verify_age_button")
            ) {
                Text("Update Age / Verify", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = TikTokGray)
            }
        }
    )
}
