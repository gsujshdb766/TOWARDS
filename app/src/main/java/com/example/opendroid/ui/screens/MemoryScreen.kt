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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.opendroid.model.MemoryCategory
import com.example.opendroid.model.MemoryNode
import com.example.opendroid.ui.components.OpenDroidTopBar
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.IndigoNeon

@Composable
fun MemoryScreen(
  repository: OpenDroidRepository
) {
  val memories by repository.memories.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf<MemoryCategory?>(null) }
  var showAddDialog by remember { mutableStateOf(false) }

  val filteredMemories = memories.filter { node ->
    val matchesCategory = selectedCategory == null || node.category == selectedCategory
    val matchesQuery = searchQuery.isBlank() ||
      node.subject.contains(searchQuery, ignoreCase = true) ||
      node.predicate.contains(searchQuery, ignoreCase = true) ||
      node.obj.contains(searchQuery, ignoreCase = true)
    matchesCategory && matchesQuery
  }

  Scaffold(
    topBar = {
      OpenDroidTopBar(
        title = "Personal Knowledge Graph",
        subtitle = "Semantic memory bank (${memories.size} nodes stored)"
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = CyanNeon,
        contentColor = Color(0xFF00363F),
        modifier = Modifier.testTag("add_memory_node_fab")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Memory")
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Search Box
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search personal memories...") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth().testTag("memory_search_input")
      )

      // Category Chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = selectedCategory == null,
          onClick = { selectedCategory = null },
          label = { Text("All") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CyanNeon.copy(alpha = 0.2f),
            selectedLabelColor = CyanNeon
          )
        )
        MemoryCategory.entries.take(3).forEach { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
            label = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() }) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = CyanNeon.copy(alpha = 0.2f),
              selectedLabelColor = CyanNeon
            )
          )
        }
      }

      // Memories List
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(filteredMemories, key = { it.id }) { node ->
          MemoryNodeCard(node = node, onDelete = { repository.deleteMemory(node.id) })
        }
      }
    }
  }

  if (showAddDialog) {
    var subject by remember { mutableStateOf("") }
    var predicate by remember { mutableStateOf("prefers") }
    var obj by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(MemoryCategory.PREFERENCES) }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Store Knowledge Node", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject (Entity)") },
            placeholder = { Text("e.g., User or Laptop") },
            modifier = Modifier.fillMaxWidth().testTag("memory_subject_input")
          )
          OutlinedTextField(
            value = predicate,
            onValueChange = { predicate = it },
            label = { Text("Predicate (Relationship)") },
            modifier = Modifier.fillMaxWidth().testTag("memory_predicate_input")
          )
          OutlinedTextField(
            value = obj,
            onValueChange = { obj = it },
            label = { Text("Object (Fact / Preference)") },
            placeholder = { Text("e.g., Dark mode in evening") },
            modifier = Modifier.fillMaxWidth().testTag("memory_object_input")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (subject.isNotBlank() && obj.isNotBlank()) {
              repository.addMemory(subject.trim(), predicate.trim(), obj.trim(), category)
              showAddDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
          modifier = Modifier.testTag("submit_memory_node_btn")
        ) {
          Text("Save to Memory", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun MemoryNodeCard(node: MemoryNode, onDelete: () -> Unit) {
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
            .size(36.dp)
            .clip(CircleShape)
            .background(IndigoNeon.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Hub,
            contentDescription = null,
            tint = IndigoNeon,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = node.subject,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = CyanNeon
            )
            Text(
              text = " ${node.predicate} ",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Text(
            text = node.obj,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${node.category.name} • Confidence ${(node.confidence * 100).toInt()}% • ${node.timestamp}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }
      }

      IconButton(onClick = onDelete) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Delete Memory",
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )
      }
    }
  }
}
