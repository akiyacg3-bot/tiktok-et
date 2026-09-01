package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.MockData
import com.example.model.CommentItem
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokDarkGray
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokInputBg
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    commentsCount: Long,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val comments = remember { mutableStateListOf<CommentItem>().apply { addAll(MockData.sampleComments) } }
    var newCommentText by remember { mutableStateOf("") }
    val quickEmojis = listOf("🔥", "❤️", "😭", "👏", "😂", "✨", "💯", "🚀", "😍")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokSheetBg,
        dragHandle = null,
        modifier = modifier.testTag("comment_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.72f)
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Header Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "${MockData.formatCount(comments.size.toLong())} comments",
                    color = TikTokWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(28.dp)
                        .testTag("close_comments_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Comments",
                        tint = TikTokWhite.copy(alpha = 0.8f)
                    )
                }
            }

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(comments, key = { it.id }) { comment ->
                    CommentRow(
                        comment = comment,
                        onLikeToggle = {
                            val index = comments.indexOfFirst { it.id == comment.id }
                            if (index != -1) {
                                val current = comments[index]
                                val newIsLiked = !current.isLiked
                                val newCount = if (newIsLiked) current.likesCount + 1 else current.likesCount - 1
                                comments[index] = current.copy(
                                    isLiked = newIsLiked,
                                    likesCount = newCount
                                )
                            }
                        }
                    )
                }
            }

            // Quick Emoji Selector Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TikTokSheetBg)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(quickEmojis) { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .clickable {
                                newCommentText += emoji
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .testTag("quick_emoji_$emoji")
                    )
                }
            }

            // Input Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E2024))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // User avatar preview
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(TikTokCyan, TikTokRed))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "U",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                TextField(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    placeholder = {
                        Text(
                            text = "Add comment...",
                            color = TikTokGray,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TikTokInputBg,
                        unfocusedContainerColor = TikTokInputBg,
                        disabledContainerColor = TikTokInputBg,
                        focusedTextColor = TikTokWhite,
                        unfocusedTextColor = TikTokWhite,
                        cursorColor = TikTokRed,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("comment_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (newCommentText.isNotBlank()) {
                            comments.add(
                                0,
                                CommentItem(
                                    id = "c_${System.currentTimeMillis()}",
                                    username = "You",
                                    userHandle = "@current_user",
                                    commentText = newCommentText.trim(),
                                    timeAgo = "Just now",
                                    likesCount = 0,
                                    isLiked = false
                                )
                            )
                            newCommentText = ""
                        }
                    },
                    enabled = newCommentText.isNotBlank(),
                    modifier = Modifier.testTag("send_comment_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Comment",
                        tint = if (newCommentText.isNotBlank()) TikTokRed else TikTokGray
                    )
                }
            }
        }
    }
}

@Composable
fun CommentRow(
    comment: CommentItem,
    onLikeToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("comment_row_${comment.id}")
    ) {
        // Commenter Avatar
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            val initial = comment.username.firstOrNull()?.uppercase() ?: "A"
            Text(
                text = initial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Comment content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.username,
                    color = TikTokGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (comment.isCreatorLiked) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TikTokRed.copy(alpha = 0.2f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Creator liked",
                            color = TikTokRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = comment.commentText,
                color = TikTokWhite,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = comment.timeAgo,
                    color = TikTokGray,
                    fontSize = 12.sp
                )
                Text(
                    text = "Reply",
                    color = TikTokGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { /* reply action */ }
                )
            }
        }

        // Like button for comment
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = onLikeToggle)
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = if (comment.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Like Comment",
                tint = if (comment.isLiked) TikTokRed else TikTokGray,
                modifier = Modifier.size(18.dp)
            )
            if (comment.likesCount > 0) {
                Text(
                    text = MockData.formatCount(comment.likesCount),
                    color = TikTokGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}
