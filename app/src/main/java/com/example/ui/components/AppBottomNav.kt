package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.common.Strings
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.Screen

data class NavItem(
    val screen: Screen,
    val icon: ImageVector,
    val labelKey: String,
    val tag: String
)

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    isHindi: Boolean,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(Screen.HOME, Icons.Default.Home, "nav_home", "nav_home_tab"),
        NavItem(Screen.TEXT_TO_VIDEO, Icons.Default.Videocam, "nav_text_video", "nav_text_tab"),
        NavItem(Screen.PHOTO_TO_VIDEO, Icons.Default.Image, "nav_photo_video", "nav_photo_tab"),
        NavItem(Screen.CREATIONS, Icons.Default.VideoLibrary, "nav_creations", "nav_creations_tab"),
        NavItem(Screen.SETTINGS, Icons.Default.Settings, "nav_settings", "nav_settings_tab")
    )

    NavigationBar(
        modifier = modifier
            .background(DarkSurface)
            .border(width = 0.5.dp, color = DarkOutline),
        containerColor = DarkSurface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = Strings.get(item.labelKey, isHindi),
                        tint = if (isSelected) NeonCyan else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = Strings.get(item.labelKey, isHindi),
                        color = if (isSelected) NeonCyan else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = DarkSurfaceVariant
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
