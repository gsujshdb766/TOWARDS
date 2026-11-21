package com.example.opendroid.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.opendroid.model.MacroItem
import com.example.opendroid.model.RoutineItem
import com.example.opendroid.model.TriggerType
import com.example.opendroid.ui.components.OpenDroidTopBar
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.IndigoNeon
import com.example.ui.theme.RoseNeon

@Composable
fun AutomationScreen(
  repository: OpenDroidRepository
) {
  val routines by repository.routines.collectAsState()
  val macros by repository.macros.collectAsState()
  var selectedTab by remember { mutableIntStateOf(0) }
  var showAddRoutineDialog by remember { mutableStateOf(false) }
  var showRecordMacroDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    OpenDroidTopBar(
      title = "Routines & Macros",
      subtitle = "Event triggers and recorded tap workflows"
    )

    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = CyanNeon,
      edgePadding = 16.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("Smart Routines (${routines.size})") },
        modifier = Modifier.testTag("tab_routines")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("UI Macros (${macros.size})") },
        modifier = Modifier.testTag("tab_macros")
      )
    }

    if (selectedTab == 0) {
      RoutinesContent(
        routines = routines,
        onToggle = { repository.toggleRoutine(it) },
        onAddClick = { showAddRoutineDialog = true }
      )
    } else {
      MacrosContent(
        macros = macros,
        onRunMacro = { repository.runMacro(it) },
        onRecordClick = { showRecordMacroDialog = true }
      )
    }
  }

  if (showAddRoutineDialog) {
    var name by remember { mutableStateOf("") }
    var trigger by remember { mutableStateOf("Time: 8:00 AM daily") }
    var action1 by remember { mutableStateOf("Open Spotify & resume daily mix") }
    var action2 by remember { mutableStateOf("Set volume to 40%") }

    AlertDialog(
      onDismissRequest = { showAddRoutineDialog = false },
      title = { Text("New Automation Routine", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Routine Name") },
            placeholder = { Text("e.g., Morning Coffee") },
            modifier = Modifier.fillMaxWidth().testTag("routine_name_input")
          )
          OutlinedTextField(
            value = trigger,
            onValueChange = { trigger = it },
            label = { Text("Trigger Description") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = action1,
            onValueChange = { action1 = it },
            label = { Text("Action 1") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = action2,
            onValueChange = { action2 = it },
            label = { Text("Action 2") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (name.isNotBlank()) {
              repository.addRoutine(
                name = name.trim(),
                triggerDesc = trigger.trim(),
                type = TriggerType.TIME,
                actions = listOf(action1, action2).filter { it.isNotBlank() }
              )
              showAddRoutineDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
          modifier = Modifier.testTag("submit_routine_button")
        ) {
          Text("Save Routine", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddRoutineDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  if (showRecordMacroDialog) {
    var name by remember { mutableStateOf("") }
    var app by remember { mutableStateOf("Instagram") }
    var steps by remember { mutableStateOf("5") }

    AlertDialog(
      onDismissRequest = { showRecordMacroDialog = false },
      title = { Text("Record UI Macro", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "OpenDroid Accessibility Service records your exact gestures, taps, and text inputs to replay automatically.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Macro Name") },
            placeholder = { Text("e.g., Post Quick Story") },
            modifier = Modifier.fillMaxWidth().testTag("macro_name_input")
          )
          OutlinedTextField(
            value = app,
            onValueChange = { app = it },
            label = { Text("Target Application") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = steps,
            onValueChange = { steps = it },
            label = { Text("Number of Step Actions") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (name.isNotBlank()) {
              val s = steps.toIntOrNull() ?: 4
              repository.addMacro(name.trim(), app.trim(), s, listOf("Automation", app.trim()))
              showRecordMacroDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = RoseNeon, contentColor = Color.White),
          modifier = Modifier.testTag("submit_macro_button")
        ) {
          Icon(imageVector = Icons.Default.FiberManualRecord, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Save Macro", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showRecordMacroDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun RoutinesContent(
  routines: List<RoutineItem>,
  onToggle: (String) -> Unit,
  onAddClick: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ACTIVE AUTOMATIONS",
          style = MaterialTheme.typography.labelSmall,
          color = CyanNeon,
          fontWeight = FontWeight.Bold
        )
        Button(
          onClick = onAddClick,
          colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
          modifier = Modifier.testTag("add_routine_btn")
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Routine", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    items(routines, key = { it.id }) { routine ->
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
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = routine.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = routine.triggerDesc,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Switch(
              checked = routine.isEnabled,
              onCheckedChange = { onToggle(routine.id) },
              colors = SwitchDefaults.colors(
                checkedThumbColor = CyanNeon,
                checkedTrackColor = CyanNeon.copy(alpha = 0.3f)
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              routine.actionsList.forEachIndexed { i, act ->
                Text(
                  text = "• $act",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Last executed: ${routine.lastRunTime}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }
      }
    }
  }
}

@Composable
private fun MacrosContent(
  macros: List<MacroItem>,
  onRunMacro: (String) -> Unit,
  onRecordClick: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ACCESSIBILITY UI TAP SEQUENCES",
          style = MaterialTheme.typography.labelSmall,
          color = RoseNeon,
          fontWeight = FontWeight.Bold
        )
        Button(
          onClick = onRecordClick,
          colors = ButtonDefaults.buttonColors(containerColor = RoseNeon, contentColor = Color.White),
          modifier = Modifier.testTag("record_macro_btn")
        ) {
          Icon(imageVector = Icons.Default.FiberManualRecord, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Record Macro", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    items(macros, key = { it.id }) { macro ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(IndigoNeon.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.TouchApp, contentDescription = null, tint = IndigoNeon, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = macro.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${macro.recordedApp} • ${macro.stepsCount} steps • Replayed ${macro.runCount} times",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Button(
            onClick = { onRunMacro(macro.id) },
            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
            modifier = Modifier.testTag("run_macro_${macro.id}")
          ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(2.dp))
            Text("Replay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
