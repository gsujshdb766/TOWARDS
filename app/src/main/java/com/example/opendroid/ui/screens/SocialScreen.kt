package com.example.opendroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opendroid.data.OpenDroidRepository
import com.example.opendroid.model.SocialAccount
import com.example.opendroid.model.SocialPlatformType
import com.example.opendroid.model.SocialPostItem
import com.example.opendroid.model.SocialPostStatus
import com.example.opendroid.ui.components.MetricStatCard
import com.example.opendroid.ui.components.OpenDroidTopBar
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.IndigoNeon

@Composable
fun SocialScreen(
  repository: OpenDroidRepository
) {
  val accounts by repository.socialAccounts.collectAsState()
  val posts by repository.socialPosts.collectAsState()
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Overview", "Composer", "Accounts", "Published & Queue")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    OpenDroidTopBar(
      title = "Social AI Hub",
      subtitle = "Multi-channel automated marketing"
    )

    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = CyanNeon,
      edgePadding = 16.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      tabs.forEachIndexed { index, tabTitle ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = tabTitle,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
            )
          },
          modifier = Modifier.testTag("social_tab_$index")
        )
      }
    }

    when (selectedTab) {
      0 -> SocialOverviewTab(accounts = accounts, posts = posts)
      1 -> SocialComposerTab(repository = repository)
      2 -> SocialAccountsTab(accounts = accounts, onToggleConnection = { repository.toggleAccountConnection(it) })
      3 -> SocialQueueTab(posts = posts)
    }
  }
}

@Composable
private fun SocialOverviewTab(
  accounts: List<SocialAccount>,
  posts: List<SocialPostItem>
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MetricStatCard(
          title = "Total Reach",
          value = "63.8K",
          trend = "+14.2% this week",
          isPositive = true,
          icon = Icons.Default.People,
          modifier = Modifier.weight(1f)
        )
        MetricStatCard(
          title = "Avg. Engagement",
          value = "5.8%",
          trend = "+2.1% AI optimized",
          isPositive = true,
          icon = Icons.Default.TrendingUp,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MetricStatCard(
          title = "Scheduled Queue",
          value = "${posts.count { it.status == SocialPostStatus.SCHEDULED }} Posts",
          trend = "Next tomorrow 8:00 AM",
          isPositive = true,
          icon = Icons.Default.Schedule,
          modifier = Modifier.weight(1f)
        )
        MetricStatCard(
          title = "Connected Hubs",
          value = "${accounts.count { it.isConnected }} Active",
          trend = "6 supported platforms",
          isPositive = true,
          icon = Icons.Default.Share,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Text(
        text = "RECENT TOP POSTS",
        style = MaterialTheme.typography.labelMedium,
        color = CyanNeon,
        fontWeight = FontWeight.Bold
      )
    }

    items(posts.take(3)) { post ->
      SocialPostCard(post = post)
    }
  }
}

@Composable
private fun SocialComposerTab(
  repository: OpenDroidRepository
) {
  var postText by remember {
    mutableStateOf("🚀 OpenDroid v1.0.7 is live! Autonomous on-device AI actions, multi-channel social management, and zero-latency local LLM routing on Android. #AndroidAI #OpenSource")
  }
  var selectedTone by remember { mutableStateOf("High-Energy") }
  val selectedPlatforms = remember {
    mutableStateListOf(SocialPlatformType.X, SocialPlatformType.LINKEDIN, SocialPlatformType.DISCORD)
  }
  val tones = listOf("High-Energy", "Professional", "Technical", "Casual")

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "TARGET CHANNELS",
        style = MaterialTheme.typography.labelSmall,
        color = CyanNeon,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(
          SocialPlatformType.X,
          SocialPlatformType.LINKEDIN,
          SocialPlatformType.DISCORD,
          SocialPlatformType.TELEGRAM
        ).forEach { platform ->
          val isSelected = selectedPlatforms.contains(platform)
          FilterChip(
            selected = isSelected,
            onClick = {
              if (isSelected) selectedPlatforms.remove(platform)
              else selectedPlatforms.add(platform)
            },
            label = { Text(platform.name) },
            leadingIcon = if (isSelected) {
              { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CyanNeon.copy(alpha = 0.2f),
              selectedLabelColor = CyanNeon
            )
          )
        }
      }
    }

    item {
      Text(
        text = "AI TONE ENHANCER",
        style = MaterialTheme.typography.labelSmall,
        color = CyanNeon,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        tones.forEach { tone ->
          FilterChip(
            selected = selectedTone == tone,
            onClick = {
              selectedTone = tone
              postText = when (tone) {
                "Professional" -> "We are pleased to introduce OpenDroid v1.0.7. An autonomous architecture for on-device AI actions, multi-platform management, and privacy-preserving execution on Android."
                "Technical" -> "OpenDroid v1.0.7: Kotlin Compose + LiteRT local model integration + Room encrypted memory graph + AccessibilityService node traversal automation. GitHub release live."
                "Casual" -> "Hey friends! OpenDroid 1.0.7 is out. Automate your phone with local AI and no cloud lag. Give it a spin!"
                else -> "🚀 OpenDroid v1.0.7 is live! Autonomous on-device AI actions, multi-channel social management, and zero-latency local LLM routing on Android. #AndroidAI #OpenSource"
              }
            },
            label = { Text(tone) }
          )
        }
      }
    }

    item {
      Text(
        text = "POST CONTENT",
        style = MaterialTheme.typography.labelSmall,
        color = CyanNeon,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = postText,
        onValueChange = { postText = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp)
          .testTag("social_composer_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = CyanNeon,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
      )
    }

    item {
      Button(
        onClick = {
          if (postText.isNotBlank()) {
            repository.publishSocialPost(postText, selectedPlatforms.toList())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("publish_social_post_button")
      ) {
        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Broadcast to ${selectedPlatforms.size} Channels", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
private fun SocialAccountsTab(
  accounts: List<SocialAccount>,
  onToggleConnection: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(accounts, key = { it.id }) { acc ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(acc.avatarColorHex)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = acc.platform.name.take(2),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = acc.handle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${acc.platform.name} • ${acc.followerCount} followers",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Switch(
            checked = acc.isConnected,
            onCheckedChange = { onToggleConnection(acc.id) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = CyanNeon,
              checkedTrackColor = CyanNeon.copy(alpha = 0.3f)
            )
          )
        }
      }
    }
  }
}

@Composable
private fun SocialQueueTab(
  posts: List<SocialPostItem>
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(posts, key = { it.id }) { post ->
      SocialPostCard(post = post)
    }
  }
}

@Composable
fun SocialPostCard(post: SocialPostItem) {
  val badgeColor = when (post.status) {
    SocialPostStatus.PUBLISHED -> EmeraldNeon
    SocialPostStatus.SCHEDULED -> CyanNeon
    SocialPostStatus.DRAFT -> MaterialTheme.colorScheme.onSurfaceVariant
    SocialPostStatus.FAILED -> MaterialTheme.colorScheme.error
  }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = post.platform.name,
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = CyanNeon
        )

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = badgeColor.copy(alpha = 0.15f)
        ) {
          Text(
            text = post.status.name,
            style = MaterialTheme.typography.labelSmall,
            color = badgeColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = post.content,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
      )

      if (post.status == SocialPostStatus.PUBLISHED) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "${post.likes}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "${post.shares}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "${post.comments}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }
}
