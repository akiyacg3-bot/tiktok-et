package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoPost
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TikTokGray
import com.example.ui.theme.TikTokRed
import com.example.ui.theme.TikTokSheetBg
import com.example.ui.theme.TikTokWhite
import kotlinx.coroutines.launch

data class ShareTarget(
    val name: String,
    val icon: ImageVector,
    val bgColor: Color
)

data class ShareAction(
    val name: String,
    val icon: ImageVector,
    val actionType: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    video: VideoPost,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val contacts = listOf(
        Pair("Sarah", Color(0xFFE91E63)),
        Pair("Devin", Color(0xFF2196F3)),
        Pair("Elena", Color(0xFFFF9800)),
        Pair("Milo", Color(0xFF4CAF50)),
        Pair("Lucas", Color(0xFF9C27B0))
    )

    val shareApps = listOf(
        ShareTarget("Repost", Icons.Default.Repeat, TikTokRed),
        ShareTarget("Copy Link", Icons.Default.Link, Color(0xFF2E7D32)),
        ShareTarget("WhatsApp", Icons.Default.Send, Color(0xFF25D366)),
        ShareTarget("Stories", Icons.Default.Share, Color(0xFFC13584)),
        ShareTarget("SMS", Icons.Default.Send, Color(0xFF0288D1)),
        ShareTarget("QR Code", Icons.Default.QrCode, Color(0xFF455A64))
    )

    val shareActions = listOf(
        ShareAction("Save Video", Icons.Default.Download, "save"),
        ShareAction("Duet", Icons.Default.Group, "duet"),
        ShareAction("Stitch", Icons.Default.ContentCopy, "stitch"),
        ShareAction("Wallpaper", Icons.Default.Wallpaper, "wallpaper"),
        ShareAction("Report", Icons.Default.Flag, "report")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TikTokSheetBg,
        dragHandle = null,
        modifier = modifier.testTag("share_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
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
                    text = "Send to",
                    color = TikTokWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp).testTag("close_share_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Share",
                        tint = TikTokWhite.copy(alpha = 0.8f)
                    )
                }
            }

            // Quick Friend Contacts
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(contacts) { (name, color) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                Toast.makeText(context, "Sent to $name!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                            .testTag("share_contact_$name")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.first().toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name,
                            color = TikTokWhite,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFF262830), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Share Target Apps
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(shareApps) { target ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                Toast.makeText(context, "${target.name} selected!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                            .testTag("share_app_${target.name}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(target.bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = target.icon,
                                contentDescription = target.name,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = target.name,
                            color = TikTokWhite,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFF262830), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Action options (Save Video, Duet, Stitch, Report)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(shareActions) { action ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                Toast.makeText(context, "${action.name} selected!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            }
                            .testTag("share_action_${action.actionType}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF262830)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.name,
                                tint = TikTokWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = action.name,
                            color = TikTokWhite,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
