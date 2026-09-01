package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCoinGold
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite

data class InboxNotification(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val iconBg: Color,
    val hasAction: Boolean = false
)

@Composable
fun InboxScreen(
    modifier: Modifier = Modifier
) {
    val notifications = listOf(
        InboxNotification("n1", "Devin Tech liked your video", "Building a full TikTok clone in Jetpack Compose...", "12m ago", Icons.Default.Favorite, TikTokRed),
        InboxNotification("n2", "Elena Vance sent you a Golden Dragon 🐉", "Sent 500 coins virtual gift on your live stream!", "45m ago", Icons.Default.MonetizationOn, TikTokCoinGold),
        InboxNotification("n3", "Sarah Jenkins followed you", "Started following your account", "2h ago", Icons.Default.PersonAdd, TikTokCyan, true),
        InboxNotification("n4", "System Notification", "Your video reached 1,000,000 views! 🔥", "1d ago", Icons.Default.Notifications, Color(0xFF673AB7)),
        InboxNotification("n5", "Lucas Vance mentioned you in a comment", "@elena_dance check out this cool animation!", "2d ago", Icons.Default.Chat, Color(0xFF009688))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .statusBarsPadding()
            .testTag("inbox_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Inbox",
                color = TikTokWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Direct Messages",
                tint = TikTokWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(notifications, key = { it.id }) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(item.iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            color = TikTokWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.description,
                            color = TikTokGray,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.time,
                            color = TikTokGray.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }

                    if (item.hasAction) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = TikTokRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(text = "Follow", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
