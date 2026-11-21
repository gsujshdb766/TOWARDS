package com.example.opendroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.opendroid.data.OpenDroidRepository
import com.example.opendroid.ui.components.OpenDroidTopBar
import com.example.opendroid.ui.components.PlanCard
import com.example.ui.theme.CyanNeon

@Composable
fun PlanScreen(
  repository: OpenDroidRepository
) {
  val plans by repository.plans.collectAsState()
  var showCreateDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      OpenDroidTopBar(
        title = "Autonomous Plans",
        subtitle = "Multi-step goal execution engine"
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showCreateDialog = true },
        containerColor = CyanNeon,
        contentColor = Color(0xFF00363F),
        modifier = Modifier.testTag("create_new_plan_button")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text("New Plan", fontWeight = FontWeight.Bold)
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Text(
        text = "ACTIVE GOALS & WORKFLOWS (${plans.size})",
        style = MaterialTheme.typography.labelMedium,
        color = CyanNeon,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(10.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(plans, key = { it.id }) { plan ->
          PlanCard(
            plan = plan,
            onAdvance = { repository.advancePlanStep(plan.id) }
          )
        }
      }
    }
  }

  if (showCreateDialog) {
    var title by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var step1 by remember { mutableStateOf("Inspect device context and state") }
    var step2 by remember { mutableStateOf("Perform core automation and notify") }
    var step3 by remember { mutableStateOf("Verify output and write memory audit") }

    AlertDialog(
      onDismissRequest = { showCreateDialog = false },
      title = {
        Text("Create Autonomous Plan", fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Plan Title") },
            placeholder = { Text("e.g., Weekly Team Sync Blast") },
            modifier = Modifier.fillMaxWidth().testTag("plan_title_input")
          )
          OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = { Text("Goal Description") },
            placeholder = { Text("Describe the desired end state...") },
            modifier = Modifier.fillMaxWidth().testTag("plan_goal_input")
          )
          Text("Execution Steps:", style = MaterialTheme.typography.labelSmall, color = CyanNeon)
          OutlinedTextField(
            value = step1,
            onValueChange = { step1 = it },
            label = { Text("Step 1") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = step2,
            onValueChange = { step2 = it },
            label = { Text("Step 2") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = step3,
            onValueChange = { step3 = it },
            label = { Text("Step 3") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) {
              repository.createNewPlan(
                title = title.trim(),
                goal = if (goal.isNotBlank()) goal.trim() else title.trim(),
                stepTitles = listOf(step1, step2, step3).filter { it.isNotBlank() }
              )
              showCreateDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
          modifier = Modifier.testTag("submit_plan_button")
        ) {
          Text("Generate Plan", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCreateDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
