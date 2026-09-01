package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokBlack
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite

data class SuggestedFriend(
    val id: String,
    val name: String,
    val handle: String,
    val mutualFriends: Int,
    val initial: String,
    val color: Color
)

@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier
) {
    val suggestedFriends = listOf(
        SuggestedFriend("f1", "Marcus Chen", "@marcus_vlogs", 12, "M", Color(0xFF3F51B5)),
        SuggestedFriend("f2", "Emma Watson", "@emma_creative", 8, "E", Color(0xFFE91E63)),
        SuggestedFriend("f3", "David Kim", "@dkim_beats", 24, "D", Color(0xFF009688)),
        SuggestedFriend("f4", "Sophia Rossi", "@sophia_art", 15, "S", Color(0xFFFF9800)),
        SuggestedFriend("f5", "Liam Walker", "@liam_skate", 5, "L", Color(0xFF9C27B0))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TikTokBlack)
            .statusBarsPadding()
            .testTag("friends_screen")
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
                text = "Friends",
                color = TikTokWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.GroupAdd,
                contentDescription = "Find Friends",
                tint = TikTokWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = "Suggested Accounts",
            color = TikTokGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(suggestedFriends, key = { it.id }) { friend ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(friend.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = friend.initial,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = friend.name,
                            color = TikTokWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = friend.handle,
                            color = TikTokGray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${friend.mutualFriends} mutual friends",
                            color = TikTokGray.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }

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
