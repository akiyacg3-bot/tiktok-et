package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokWhite

enum class FeedTab {
    FOLLOWING,
    FRIENDS,
    FOR_YOU
}

@Composable
fun TopHeaderBar(
    selectedTab: FeedTab,
    onTabSelected: (FeedTab) -> Unit,
    onLiveClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("top_header_bar")
    ) {
        // LIVE Button on Left
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(onClick = onLiveClick)
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .testTag("live_button")
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(TikTokRed)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "LIVE",
                color = TikTokWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Center Feed Tabs: Following | Friends | For You
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FeedTabItem(
                title = "Following",
                isSelected = selectedTab == FeedTab.FOLLOWING,
                onClick = { onTabSelected(FeedTab.FOLLOWING) },
                testTag = "tab_following"
            )
            FeedTabItem(
                title = "Friends",
                isSelected = selectedTab == FeedTab.FRIENDS,
                onClick = { onTabSelected(FeedTab.FRIENDS) },
                testTag = "tab_friends"
            )
            FeedTabItem(
                title = "For You",
                isSelected = selectedTab == FeedTab.FOR_YOU,
                onClick = { onTabSelected(FeedTab.FOR_YOU) },
                testTag = "tab_foryou"
            )
        }

        // Search Button on Right
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(36.dp)
                .testTag("search_button")
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TikTokWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FeedTabItem(
    title: String,
    isSelected: Boolean,
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
            .padding(horizontal = 2.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Text(
            text = title,
            color = if (isSelected) TikTokWhite else TikTokWhite.copy(alpha = 0.60f),
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(TikTokWhite)
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
