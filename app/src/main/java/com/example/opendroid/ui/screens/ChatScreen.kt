package com.example.opendroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.opendroid.ui.components.MessageBubble
import com.example.opendroid.ui.components.OpenDroidTopBar
import com.example.opendroid.ui.components.QuickPromptChip
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.IndigoNeon
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
  repository: OpenDroidRepository,
  onNavigateToSettings: () -> Unit
) {
  val messages by repository.chatMessages.collectAsState()
  val activeProvider by repository.activeProvider.collectAsState()
  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  var inputQuery by remember { mutableStateOf("") }

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    OpenDroidTopBar(
      title = "OpenDroid AI",
      activeModelName = activeProvider.displayName,
      actions = {
        IconButton(
          onClick = { repository.clearChat() },
          modifier = Modifier.testTag("clear_chat_button")
        ) {
          Icon(
            imageVector = Icons.Default.DeleteSweep,
            contentDescription = "Clear Chat",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        IconButton(
          onClick = onNavigateToSettings,
          modifier = Modifier.testTag("chat_settings_button")
        ) {
          Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    )

    // Quick Suggestions Carousel
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      QuickPromptChip(text = "Turn off WiFi & toggle DND") {
        scope.launch { repository.sendUserMessage("Turn off WiFi & toggle DND") }
      }
      QuickPromptChip(text = "Draft social post for OpenDroid launch") {
        scope.launch { repository.sendUserMessage("Draft social post for OpenDroid launch") }
      }
      QuickPromptChip(text = "Create multi-step launch plan") {
        scope.launch { repository.sendUserMessage("Create multi-step launch plan") }
      }
      QuickPromptChip(text = "Trigger Morning Routine") {
        scope.launch { repository.sendUserMessage("Trigger Morning Routine") }
      }
    }

    // Message Feed
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      if (messages.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(CyanNeon.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = null,
              tint = CyanNeon,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Autonomous AI Agent Ready",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Type an instruction or choose a quick prompt to automate Android device actions, social posts, and routines.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      } else {
        LazyColumn(
          state = listState,
          modifier = Modifier.fillMaxSize()
        ) {
          items(messages, key = { it.id }) { message ->
            MessageBubble(
              message = message,
              onApproveAction = { repository.approveAction(it) },
              onDenyAction = { repository.denyAction(it) },
              onAdvancePlan = { repository.advancePlanStep(it) }
            )
          }
        }
      }
    }

    // Input Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth()
        .imePadding()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = inputQuery,
          onValueChange = { inputQuery = it },
          placeholder = {
            Text(
              text = "Ask OpenDroid or command action...",
              style = MaterialTheme.typography.bodyMedium,
              fontSize = 13.sp
            )
          },
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyanNeon,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .weight(1f)
            .testTag("chat_input_field")
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Send Button
        IconButton(
          onClick = {
            if (inputQuery.isNotBlank()) {
              val text = inputQuery.trim()
              inputQuery = ""
              scope.launch { repository.sendUserMessage(text) }
            }
          },
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (inputQuery.isNotBlank()) CyanNeon else MaterialTheme.colorScheme.surfaceVariant)
            .testTag("send_message_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send",
            tint = if (inputQuery.isNotBlank()) Color(0xFF00363F) else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}
