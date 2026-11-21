package com.example.opendroid.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opendroid.data.OpenDroidRepository
import com.example.opendroid.model.AutoApprovalMode
import com.example.opendroid.model.LLMProviderType
import com.example.opendroid.model.OnDeviceModelInfo
import com.example.opendroid.ui.components.OpenDroidTopBar
import com.example.ui.theme.AmberNeon
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.RoseNeon
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
  repository: OpenDroidRepository
) {
  val activeProvider by repository.activeProvider.collectAsState()
  val autoApprovalMode by repository.autoApprovalMode.collectAsState()
  val temperature by repository.temperature.collectAsState()
  val onDeviceModels by repository.onDeviceModels.collectAsState()
  val lastBenchmark by repository.lastBenchmark.collectAsState()
  val crashLogs by repository.crashLogs.collectAsState()
  val scope = rememberCoroutineScope()
  var isBenchmarking by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    OpenDroidTopBar(
      title = "Agent Settings",
      subtitle = "LLM Engine, Security, and Benchmarks"
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Active Provider Selection
      item {
        SectionTitle(title = "ACTIVE LLM INFERENCE ENGINE")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            LLMProviderType.entries.forEach { provider ->
              val isSelected = activeProvider == provider
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) CyanNeon.copy(alpha = 0.15f) else Color.Transparent,
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, CyanNeon) else null,
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { repository.setActiveProvider(provider) }
                  .testTag("provider_option_${provider.name}")
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column {
                    Text(
                      text = provider.displayName,
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) CyanNeon else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = provider.badge,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  if (isSelected) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                  }
                }
              }
            }
          }
        }
      }

      // 2. Auto-Approval Policy
      item {
        SectionTitle(title = "SAFETY & AUTO-APPROVAL POLICY")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AutoApprovalMode.entries.forEach { mode ->
              val isSelected = autoApprovalMode == mode
              val color = when (mode) {
                AutoApprovalMode.CONSERVATIVE -> EmeraldNeon
                AutoApprovalMode.BALANCED -> CyanNeon
                AutoApprovalMode.AUTONOMOUS -> AmberNeon
              }
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) color.copy(alpha = 0.12f) else Color.Transparent,
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, color) else null,
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { repository.setAutoApprovalMode(mode) }
                  .testTag("approval_mode_${mode.name}")
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = mode.title,
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold,
                      color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = mode.description,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  if (isSelected) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = color)
                  }
                }
              }
            }
          }
        }
      }

      // 3. Inference Temperature
      item {
        SectionTitle(title = "TEMPERATURE & CREATIVITY: ${"%.1f".format(temperature)}")
        Spacer(modifier = Modifier.height(4.dp))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Slider(
              value = temperature,
              onValueChange = { repository.setTemperature(it) },
              valueRange = 0.0f..1.2f,
              steps = 11,
              colors = SliderDefaults.colors(
                thumbColor = CyanNeon,
                activeTrackColor = CyanNeon
              ),
              modifier = Modifier.testTag("temperature_slider")
            )
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("0.0 (Deterministic / Code)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("1.2 (Creative)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        }
      }

      // 4. On-Device Downloadable Models Hub
      item {
        SectionTitle(title = "ON-DEVICE OFFLINE MODEL HUB")
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          onDeviceModels.forEach { model ->
            OnDeviceModelCard(
              model = model,
              onDownload = {
                scope.launch { repository.downloadOnDeviceModel(model.id) }
              }
            )
          }
        }
      }

      // 5. Benchmark Runner
      item {
        SectionTitle(title = "LOCAL AI HARDWARE BENCHMARK")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            if (lastBenchmark != null) {
              Text(
                text = "Model: ${lastBenchmark?.modelTested}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                BenchmarkBadge(label = "First Token", value = "${lastBenchmark?.firstTokenLatencyMs} ms")
                BenchmarkBadge(label = "Speed", value = "${lastBenchmark?.tokensPerSec} t/s")
                BenchmarkBadge(label = "RAM Usage", value = "${lastBenchmark?.ramUsageMb} MB")
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
              onClick = {
                isBenchmarking = true
                scope.launch {
                  repository.runBenchmark(activeProvider.displayName)
                  isBenchmarking = false
                }
              },
              enabled = !isBenchmarking,
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("run_benchmark_button")
            ) {
              Icon(imageVector = Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (isBenchmarking) "Running Benchmark..." else "Execute Hardware Inference Benchmark", fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // 6. System & App Info
      item {
        SectionTitle(title = "OPENDROID ARCHITECTURE SPECS")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            SpecRow("App Version", "v1.0.7 (Native Android APK)")
            SpecRow("Build Platform", "Android OS (Kotlin + Jetpack Compose)")
            SpecRow("Automation Engine", "OpenDroid Accessibility Service")
            SpecRow("Privacy & Encryption", "AES-256 GCM (AndroidKeyStore)")
            SpecRow("Local Database", "Room Persistence 2.7.0")
          }
        }
      }
    }
  }
}

@Composable
fun SectionTitle(title: String) {
  Text(
    text = title,
    style = MaterialTheme.typography.labelMedium,
    color = CyanNeon,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.sp
  )
}

@Composable
fun OnDeviceModelCard(
  model: OnDeviceModelInfo,
  onDownload: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = model.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${model.parameters} • Size: ${model.size}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (model.isDownloaded) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = EmeraldNeon.copy(alpha = 0.15f)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(imageVector = Icons.Default.DownloadDone, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Ready", color = EmeraldNeon, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
          }
        } else {
          Button(
            onClick = onDownload,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = CyanNeon),
            modifier = Modifier.testTag("download_model_${model.id}")
          ) {
            Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Download", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      if (!model.isDownloaded && model.downloadProgress > 0f) {
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { model.downloadProgress },
          color = CyanNeon,
          modifier = Modifier.fillMaxWidth().height(4.dp)
        )
      }
    }
  }
}

@Composable
fun BenchmarkBadge(label: String, value: String) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    modifier = Modifier.padding(vertical = 2.dp)
  ) {
    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
      Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = CyanNeon)
    }
  }
}

@Composable
fun SpecRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
  }
}
