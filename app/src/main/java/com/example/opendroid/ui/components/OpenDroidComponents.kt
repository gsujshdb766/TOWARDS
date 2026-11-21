package com.example.opendroid.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opendroid.model.ActionCategory
import com.example.opendroid.model.ActionStatus
import com.example.opendroid.model.ChatMessage
import com.example.opendroid.model.MessageSender
import com.example.opendroid.model.PlanItem
import com.example.opendroid.model.PlanStatus
import com.example.opendroid.model.PlanStepItem
import com.example.opendroid.model.PlanStepStatus
import com.example.opendroid.model.RiskLevel
import com.example.opendroid.model.ToolActionItem
import com.example.ui.theme.AmberNeon
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.IndigoNeon
import com.example.ui.theme.RoseNeon

@Composable
fun OpenDroidTopBar(
  title: String,
  subtitle: String? = null,
  activeModelName: String = "Gemini Pro",
  onSettingsClick: (() -> Unit)? = null,
  actions: @Composable () -> Unit = {}
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Surface(
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                colors = listOf(CyanNeon, IndigoNeon)
              )
            )
        ) {
          Box(
            modifier = Modifier
              .size(14.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(Color.White)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = CyanNeon.copy(alpha = 0.15f)
            ) {
              Text(
                text = "ONLINE",
                color = CyanNeon,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
          if (subtitle != null) {
            Text(
              text = subtitle,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            Text(
              text = "Model: $activeModelName",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        actions()
      }
    }
  }
}

@Composable
fun MessageBubble(
  message: ChatMessage,
  onApproveAction: (String) -> Unit,
  onDenyAction: (String) -> Unit,
  onAdvancePlan: (String) -> Unit
) {
  val isUser = message.sender == MessageSender.USER
  val align = if (isUser) Alignment.End else Alignment.Start

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 6.dp),
    horizontalAlignment = align
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 2.dp)
    ) {
      if (!isUser) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          tint = CyanNeon,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "OpenDroid Agent",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = CyanNeon
        )
      } else {
        Text(
          text = "You",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    val bubbleBg = if (isUser) {
      MaterialTheme.colorScheme.primaryContainer
    } else {
      MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isUser) {
      MaterialTheme.colorScheme.onPrimaryContainer
    } else {
      MaterialTheme.colorScheme.onSurface
    }

    Surface(
      shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 4.dp,
        bottomEnd = if (isUser) 4.dp else 16.dp
      ),
      color = bubbleBg,
      tonalElevation = if (isUser) 2.dp else 1.dp,
      modifier = Modifier.testTag("chat_bubble_${message.id}")
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = message.text,
          style = MaterialTheme.typography.bodyMedium,
          color = textColor
        )

        // Attached Tool Actions
        if (message.toolActions.isNotEmpty()) {
          Spacer(modifier = Modifier.height(10.dp))
          message.toolActions.forEach { action ->
            ActionCard(
              action = action,
              onApprove = { onApproveAction(action.id) },
              onDeny = { onDenyAction(action.id) }
            )
            Spacer(modifier = Modifier.height(6.dp))
          }
        }

        // Attached Plan
        if (message.plan != null) {
          Spacer(modifier = Modifier.height(10.dp))
          PlanCard(
            plan = message.plan,
            onAdvance = { onAdvancePlan(message.plan.id) }
          )
        }
      }
    }
  }
}

@Composable
fun ActionCard(
  action: ToolActionItem,
  onApprove: () -> Unit,
  onDeny: () -> Unit
) {
  val riskColor = when (action.riskLevel) {
    RiskLevel.LOW -> EmeraldNeon
    RiskLevel.MEDIUM -> AmberNeon
    RiskLevel.HIGH -> RoseNeon
  }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("action_card_${action.id}")
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(riskColor)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = action.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = riskColor.copy(alpha = 0.15f)
        ) {
          Text(
            text = "${action.riskLevel} RISK",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = riskColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = action.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (action.parameters.isNotEmpty()) {
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(6.dp)) {
            action.parameters.forEach { (k, v) ->
              Text(
                text = "$k: $v",
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                fontSize = 11.sp,
                color = CyanNeon
              )
            }
          }
        }
      }

      if (action.executionResult != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = EmeraldNeon,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = action.executionResult,
            style = MaterialTheme.typography.bodySmall,
            color = EmeraldNeon
          )
        }
      }

      if (action.status == ActionStatus.PENDING) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDeny,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.testTag("deny_button_${action.id}")
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Deny", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Deny")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = onApprove,
            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
            modifier = Modifier.testTag("approve_button_${action.id}")
          ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Approve", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Approve", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun PlanCard(
  plan: PlanItem,
  onAdvance: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(true) }

  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.5f)),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("plan_card_${plan.id}")
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { isExpanded = !isExpanded },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = plan.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = plan.goalDescription,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (isExpanded) 3 else 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        IconButton(onClick = { isExpanded = !isExpanded }) {
          Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = CyanNeon
          )
        }
      }

      val completedCount = plan.steps.count { it.status == PlanStepStatus.DONE }
      val totalCount = plan.steps.size
      val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

      Spacer(modifier = Modifier.height(8.dp))
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = CyanNeon,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
      )

      AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          plan.steps.forEach { step ->
            PlanStepRow(step = step)
            Spacer(modifier = Modifier.height(6.dp))
          }

          if (plan.status == PlanStatus.RUNNING && completedCount < totalCount) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
              onClick = onAdvance,
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = Color(0xFF00363F)),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("advance_plan_step")
            ) {
              Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Advance Next Step ($completedCount/$totalCount)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
fun PlanStepRow(step: PlanStepItem) {
  val iconAndColor = when (step.status) {
    PlanStepStatus.DONE -> Pair(Icons.Default.Done, EmeraldNeon)
    PlanStepStatus.RUNNING -> Pair(Icons.Default.PlayArrow, CyanNeon)
    PlanStepStatus.PENDING -> Pair(Icons.Default.Warning, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
    PlanStepStatus.FAILED -> Pair(Icons.Default.ErrorOutline, RoseNeon)
  }

  Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(iconAndColor.second.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = iconAndColor.first,
          contentDescription = null,
          tint = iconAndColor.second,
          modifier = Modifier.size(14.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "${step.stepIndex}. ${step.actionTitle}",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
        if (step.outputLog != null) {
          Text(
            text = step.outputLog,
            style = MaterialTheme.typography.labelSmall,
            color = iconAndColor.second
          )
        }
      }

      if (step.status == PlanStepStatus.RUNNING) {
        CircularProgressIndicator(
          modifier = Modifier.size(14.dp),
          color = CyanNeon,
          strokeWidth = 2.dp
        )
      }
    }
  }
}

@Composable
fun MetricStatCard(
  title: String,
  value: String,
  trend: String? = null,
  isPositive: Boolean = true,
  icon: ImageVector,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = CyanNeon,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      if (trend != null) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = trend,
          style = MaterialTheme.typography.labelSmall,
          color = if (isPositive) EmeraldNeon else RoseNeon,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}

@Composable
fun QuickPromptChip(
  text: String,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)),
    modifier = Modifier
      .clickable { onClick() }
      .testTag("quick_prompt_${text.take(10)}")
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = CyanNeon,
        modifier = Modifier.size(12.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}
