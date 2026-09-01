package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.model.TikTokTab
import com.example.ui.components.TikTokBottomNav
import com.example.ui.screens.CameraRecordMode
import com.example.ui.screens.CreateVideoScreen
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.FriendsScreen
import com.example.ui.screens.InboxScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TikTokBlack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TikTokAppRoot()
            }
        }
    }
}

@Composable
fun TikTokAppRoot() {
    var currentTab by remember { mutableStateOf(TikTokTab.HOME) }
    var previousTab by remember { mutableStateOf(TikTokTab.HOME) }
    var cameraMode by remember { mutableStateOf(CameraRecordMode.FIFTEEN_S) }

    // Intercept back button if not on HOME
    BackHandler(enabled = currentTab != TikTokTab.HOME) {
        currentTab = TikTokTab.HOME
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TikTokBlack)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = TikTokBlack,
            bottomBar = {
                // Show bottom navigation bar on all main tabs except camera record mode
                if (currentTab != TikTokTab.CREATE) {
                    TikTokBottomNav(
                        selectedTab = currentTab,
                        onTabSelected = { tab ->
                            if (tab == TikTokTab.CREATE) {
                                previousTab = currentTab
                                cameraMode = CameraRecordMode.FIFTEEN_S
                            }
                            currentTab = tab
                        },
                        onGoLiveRequested = {
                            previousTab = currentTab
                            cameraMode = CameraRecordMode.LIVE
                            currentTab = TikTokTab.CREATE
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = if (currentTab != TikTokTab.CREATE) innerPadding.calculateBottomPadding() else androidx.compose.ui.unit.Dp(0f)
                    )
            ) {
                when (currentTab) {
                    TikTokTab.HOME -> {
                        FeedScreen(
                            onNavigateToProfile = { currentTab = TikTokTab.PROFILE },
                            onGoLiveDirect = {
                                previousTab = currentTab
                                cameraMode = CameraRecordMode.LIVE
                                currentTab = TikTokTab.CREATE
                            }
                        )
                    }
                    TikTokTab.FRIENDS -> {
                        FriendsScreen()
                    }
                    TikTokTab.CREATE -> {
                        CreateVideoScreen(
                            initialMode = cameraMode,
                            onClose = { currentTab = previousTab }
                        )
                    }
                    TikTokTab.INBOX -> {
                        InboxScreen()
                    }
                    TikTokTab.PROFILE -> {
                        ProfileScreen(
                            onBack = { currentTab = TikTokTab.HOME }
                        )
                    }
                }
            }
        }
    }
}
