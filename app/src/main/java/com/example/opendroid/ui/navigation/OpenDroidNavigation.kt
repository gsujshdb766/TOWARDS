package com.example.opendroid.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.opendroid.data.OpenDroidRepository
import com.example.opendroid.ui.screens.AutomationScreen
import com.example.opendroid.ui.screens.ChatScreen
import com.example.opendroid.ui.screens.MemoryScreen
import com.example.opendroid.ui.screens.PlanScreen
import com.example.opendroid.ui.screens.SettingsScreen
import com.example.opendroid.ui.screens.SocialScreen
import com.example.ui.theme.CyanNeon

enum class Screen(val title: String, val icon: ImageVector) {
  CHAT("Chat", Icons.Default.Chat),
  PLANS("Plans", Icons.Default.Checklist),
  SOCIAL("Social", Icons.Default.Share),
  AUTOMATION("Automate", Icons.Default.Bolt),
  MEMORY("Memory", Icons.Default.Hub),
  SETTINGS("Settings", Icons.Default.Settings)
}

@Composable
fun OpenDroidApp(repository: OpenDroidRepository = remember { OpenDroidRepository() }) {
  var currentScreen by remember { mutableStateOf(Screen.CHAT) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
      ) {
        val navItems = listOf(Screen.CHAT, Screen.PLANS, Screen.SOCIAL, Screen.AUTOMATION, Screen.MEMORY, Screen.SETTINGS)
        navItems.forEach { screen ->
          val isSelected = currentScreen == screen
          NavigationBarItem(
            selected = isSelected,
            onClick = { currentScreen = screen },
            icon = {
              Icon(
                imageVector = screen.icon,
                contentDescription = screen.title
              )
            },
            label = {
              Text(
                text = screen.title,
                style = MaterialTheme.typography.labelSmall
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = CyanNeon,
              selectedTextColor = CyanNeon,
              indicatorColor = CyanNeon.copy(alpha = 0.15f),
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
          )
        }
      }
    }
  ) { innerPadding ->
    androidx.compose.foundation.layout.Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        Screen.CHAT -> ChatScreen(
          repository = repository,
          onNavigateToSettings = { currentScreen = Screen.SETTINGS }
        )
        Screen.PLANS -> PlanScreen(repository = repository)
        Screen.SOCIAL -> SocialScreen(repository = repository)
        Screen.AUTOMATION -> AutomationScreen(repository = repository)
        Screen.MEMORY -> MemoryScreen(repository = repository)
        Screen.SETTINGS -> SettingsScreen(repository = repository)
      }
    }
  }
}
