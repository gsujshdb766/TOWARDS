package com.example.opendroid.data

import com.example.opendroid.model.ActionCategory
import com.example.opendroid.model.ActionStatus
import com.example.opendroid.model.AutoApprovalMode
import com.example.opendroid.model.BenchmarkResult
import com.example.opendroid.model.ChatMessage
import com.example.opendroid.model.CrashLogEntry
import com.example.opendroid.model.LLMProviderType
import com.example.opendroid.model.MacroItem
import com.example.opendroid.model.MemoryCategory
import com.example.opendroid.model.MemoryNode
import com.example.opendroid.model.MessageSender
import com.example.opendroid.model.OnDeviceModelInfo
import com.example.opendroid.model.PlanItem
import com.example.opendroid.model.PlanStatus
import com.example.opendroid.model.PlanStepItem
import com.example.opendroid.model.PlanStepStatus
import com.example.opendroid.model.RiskLevel
import com.example.opendroid.model.RoutineItem
import com.example.opendroid.model.SocialAccount
import com.example.opendroid.model.SocialPlatformType
import com.example.opendroid.model.SocialPostItem
import com.example.opendroid.model.SocialPostStatus
import com.example.opendroid.model.ToolActionItem
import com.example.opendroid.model.TriggerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class OpenDroidRepository {

  // --- Chat State ---
  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage(
        id = UUID.randomUUID().toString(),
        text = "Hello! I am OpenDroid, your autonomous Android AI assistant. I can automate device actions, manage your social channels, run routines, execute complex multi-step plans, and operate both offline with on-device models or via cloud providers.",
        sender = MessageSender.AGENT,
        toolActions = listOf(
          ToolActionItem(
            id = "sys_init_check",
            title = "Device Health & Permissions",
            description = "Accessibility Service & Notification bridge operational",
            category = ActionCategory.SYSTEM,
            riskLevel = RiskLevel.LOW,
            status = ActionStatus.COMPLETED,
            executionResult = "Battery: 85%, WiFi: Connected, Secure Storage: Ready"
          )
        )
      )
    )
  )
  val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

  // --- Automation / Routines State ---
  private val _routines = MutableStateFlow<List<RoutineItem>>(
    listOf(
      RoutineItem(
        id = "routine_morning",
        name = "Morning Launchpad",
        triggerDesc = "Weekdays at 7:00 AM",
        triggerType = TriggerType.TIME,
        actionsList = listOf("Disable Do Not Disturb", "Brief unread notifications", "Speak weather & top agenda"),
        isEnabled = true,
        lastRunTime = "Today, 7:00 AM"
      ),
      RoutineItem(
        id = "routine_focus",
        name = "Deep Focus Shield",
        triggerDesc = "When entering Work WiFi or location",
        triggerType = TriggerType.WIFI,
        actionsList = listOf("Enable Do Not Disturb", "Auto-reply on Telegram: 'In focus mode'", "Lower screen brightness"),
        isEnabled = true,
        lastRunTime = "Yesterday, 9:15 AM"
      ),
      RoutineItem(
        id = "routine_night",
        name = "Night Wind Down",
        triggerDesc = "Daily at 11:00 PM or Battery Charging",
        triggerType = TriggerType.BATTERY,
        actionsList = listOf("Turn off smart lights", "Set phone to silent", "Generate tomorrow's agenda plan"),
        isEnabled = false,
        lastRunTime = "2 days ago"
      )
    )
  )
  val routines: StateFlow<List<RoutineItem>> = _routines.asStateFlow()

  // --- Macros State ---
  private val _macros = MutableStateFlow<List<MacroItem>>(
    listOf(
      MacroItem(
        id = "macro_1",
        name = "Insta Story Reshare",
        stepsCount = 6,
        recordedApp = "Instagram",
        tags = listOf("Social", "Automation"),
        runCount = 14,
        isFavorite = true
      ),
      MacroItem(
        id = "macro_2",
        name = "Clean Storage Cache",
        stepsCount = 4,
        recordedApp = "Android Settings",
        tags = listOf("System", "Utility"),
        runCount = 8,
        isFavorite = true
      ),
      MacroItem(
        id = "macro_3",
        name = "Quick Telegram Audio Memo",
        stepsCount = 5,
        recordedApp = "Telegram",
        tags = listOf("Messaging"),
        runCount = 22,
        isFavorite = false
      )
    )
  )
  val macros: StateFlow<List<MacroItem>> = _macros.asStateFlow()

  // --- Plans State ---
  private val _plans = MutableStateFlow<List<PlanItem>>(
    listOf(
      PlanItem(
        id = "plan_marketing_blast",
        title = "AI App Product Hunt Launch Campaign",
        goalDescription = "Compose announcements across X, LinkedIn, and Discord, notify subscribers, and track metrics.",
        status = PlanStatus.RUNNING,
        steps = listOf(
          PlanStepItem(
            stepIndex = 1,
            actionTitle = "Generate Multi-Platform Post Copy",
            detail = "Use Gemini/Claude to draft platform-optimized launch copy",
            status = PlanStepStatus.DONE,
            outputLog = "Generated 3 variants tailored for X (280 chars) and LinkedIn (long form)"
          ),
          PlanStepItem(
            stepIndex = 2,
            actionTitle = "Review and Schedule on X & LinkedIn",
            detail = "Queue posts for optimal morning engagement window",
            status = PlanStepStatus.RUNNING,
            outputLog = "Awaiting user confirmation or auto-schedule trigger"
          ),
          PlanStepItem(
            stepIndex = 3,
            actionTitle = "Notify Discord VIP Community",
            detail = "Post release changelog to announcements channel",
            status = PlanStepStatus.PENDING
          ),
          PlanStepItem(
            stepIndex = 4,
            actionTitle = "Aggregate First Hour Analytics",
            detail = "Scrape engagement metrics and report sentiment",
            status = PlanStepStatus.PENDING
          )
        )
      )
    )
  )
  val plans: StateFlow<List<PlanItem>> = _plans.asStateFlow()

  // --- Social Accounts State ---
  private val _socialAccounts = MutableStateFlow<List<SocialAccount>>(
    listOf(
      SocialAccount(
        id = "acc_x",
        platform = SocialPlatformType.X,
        handle = "@opendroid_ai",
        isConnected = true,
        followerCount = "14.2K",
        avatarColorHex = 0xFF000000
      ),
      SocialAccount(
        id = "acc_li",
        platform = SocialPlatformType.LINKEDIN,
        handle = "OpenDroid Autonomous AI",
        isConnected = true,
        followerCount = "8.6K",
        avatarColorHex = 0xFF0A66C2
      ),
      SocialAccount(
        id = "acc_ig",
        platform = SocialPlatformType.INSTAGRAM,
        handle = "@opendroid.labs",
        isConnected = true,
        followerCount = "21.5K",
        avatarColorHex = 0xFFE1306C
      ),
      SocialAccount(
        id = "acc_yt",
        platform = SocialPlatformType.YOUTUBE,
        handle = "OpenDroid AI Dev",
        isConnected = false,
        followerCount = "3.2K",
        avatarColorHex = 0xFFFF0000
      ),
      SocialAccount(
        id = "acc_dc",
        platform = SocialPlatformType.DISCORD,
        handle = "OpenDroid Community",
        isConnected = true,
        followerCount = "4.9K",
        avatarColorHex = 0xFF5865F2
      ),
      SocialAccount(
        id = "acc_tg",
        platform = SocialPlatformType.TELEGRAM,
        handle = "t.me/opendroid_channel",
        isConnected = true,
        followerCount = "11.1K",
        avatarColorHex = 0xFF229ED9
      )
    )
  )
  val socialAccounts: StateFlow<List<SocialAccount>> = _socialAccounts.asStateFlow()

  // --- Social Posts State ---
  private val _socialPosts = MutableStateFlow<List<SocialPostItem>>(
    listOf(
      SocialPostItem(
        id = "post_1",
        platform = SocialPlatformType.X,
        content = "🚀 OpenDroid v1.0.7 is live! Autonomous on-device AI actions, multi-channel social management, and zero-latency local LLM routing on Android. #AndroidAI #OpenSource",
        scheduledTime = "Today, 10:30 AM",
        status = SocialPostStatus.PUBLISHED,
        likes = 342,
        shares = 89,
        comments = 45
      ),
      SocialPostItem(
        id = "post_2",
        platform = SocialPlatformType.LINKEDIN,
        content = "Excited to introduce OpenDroid: bridging accessibility automation with local LLMs on Android to execute complex user plans safely on device. Read our open architecture report below.",
        scheduledTime = "Tomorrow, 8:00 AM",
        status = SocialPostStatus.SCHEDULED,
        likes = 184,
        shares = 24,
        comments = 19
      ),
      SocialPostItem(
        id = "post_3",
        platform = SocialPlatformType.DISCORD,
        content = "Hey everyone! The new Macro recording engine allows you to record custom UI taps and trigger them with voice commands or LLM function calls.",
        scheduledTime = null,
        status = SocialPostStatus.DRAFT,
        likes = 0,
        shares = 0,
        comments = 0
      )
    )
  )
  val socialPosts: StateFlow<List<SocialPostItem>> = _socialPosts.asStateFlow()

  // --- Memory State ---
  private val _memories = MutableStateFlow<List<MemoryNode>>(
    listOf(
      MemoryNode(
        id = "mem_1",
        subject = "User",
        predicate = "prefers provider",
        obj = "Google Gemini for general reasoning, Gemma for offline mode",
        category = MemoryCategory.PREFERENCES,
        confidence = 0.98f,
        timestamp = "Just now"
      ),
      MemoryNode(
        id = "mem_2",
        subject = "Work Schedule",
        predicate = "focus interval",
        obj = "9:00 AM - 12:30 PM weekdays",
        category = MemoryCategory.WORK,
        confidence = 0.95f,
        timestamp = "Yesterday"
      ),
      MemoryNode(
        id = "mem_3",
        subject = "Social Campaign",
        predicate = "active target",
        obj = "OpenDroid Android AI release",
        category = MemoryCategory.WORK,
        confidence = 0.99f,
        timestamp = "Today"
      ),
      MemoryNode(
        id = "mem_4",
        subject = "Smart Devices",
        predicate = "living room lights",
        obj = "Philips Hue bridge on local subnet",
        category = MemoryCategory.TECH,
        confidence = 0.92f,
        timestamp = "3 days ago"
      ),
      MemoryNode(
        id = "mem_5",
        subject = "Fitness Habit",
        predicate = "evening run",
        obj = "Tuesdays & Thursdays 6:30 PM",
        category = MemoryCategory.HABITS,
        confidence = 0.88f,
        timestamp = "5 days ago"
      )
    )
  )
  val memories: StateFlow<List<MemoryNode>> = _memories.asStateFlow()

  // --- Provider & Model Settings State ---
  private val _activeProvider = MutableStateFlow(LLMProviderType.GEMINI)
  val activeProvider: StateFlow<LLMProviderType> = _activeProvider.asStateFlow()

  private val _autoApprovalMode = MutableStateFlow(AutoApprovalMode.BALANCED)
  val autoApprovalMode: StateFlow<AutoApprovalMode> = _autoApprovalMode.asStateFlow()

  private val _temperature = MutableStateFlow(0.7f)
  val temperature: StateFlow<Float> = _temperature.asStateFlow()

  private val _onDeviceModels = MutableStateFlow<List<OnDeviceModelInfo>>(
    listOf(
      OnDeviceModelInfo(
        id = "gemma_2b_it",
        name = "Gemma 2B Instruct (LiteRT)",
        size = "1.4 GB",
        parameters = "2 Billion",
        isDownloaded = true,
        downloadProgress = 1f,
        isQuantized = true
      ),
      OnDeviceModelInfo(
        id = "qwen_2_5_coder",
        name = "Qwen 2.5 Coder 1.5B (GGUF)",
        size = "980 MB",
        parameters = "1.5 Billion",
        isDownloaded = false,
        downloadProgress = 0f,
        isQuantized = true
      ),
      OnDeviceModelInfo(
        id = "tinyllama_1_1b",
        name = "TinyLlama 1.1B UltraFast",
        size = "640 MB",
        parameters = "1.1 Billion",
        isDownloaded = false,
        downloadProgress = 0f,
        isQuantized = true
      )
    )
  )
  val onDeviceModels: StateFlow<List<OnDeviceModelInfo>> = _onDeviceModels.asStateFlow()

  private val _lastBenchmark = MutableStateFlow<BenchmarkResult?>(
    BenchmarkResult(
      modelTested = "Gemma 2B Instruct (Int4 LiteRT)",
      firstTokenLatencyMs = 210,
      tokensPerSec = 28.4f,
      ramUsageMb = 840,
      temperatureCelsius = 37.2f
    )
  )
  val lastBenchmark: StateFlow<BenchmarkResult?> = _lastBenchmark.asStateFlow()

  // --- Diagnostics / Crash Logs ---
  private val _crashLogs = MutableStateFlow<List<CrashLogEntry>>(
    listOf(
      CrashLogEntry(
        id = "log_01",
        timestamp = "10:14 AM",
        tag = "AgentLoop",
        message = "Autonomous plan step 1 verified successfully with low-risk rating",
        stackTrace = "com.example.opendroid.agent.AgentLoop.verifyPlan() at line 84",
        isFatal = false
      ),
      CrashLogEntry(
        id = "log_02",
        timestamp = "09:45 AM",
        tag = "SocialManager",
        message = "X API token refreshed and cached in AndroidKeyStore",
        stackTrace = "com.example.opendroid.social.XAdapter.refreshToken() at line 42",
        isFatal = false
      )
    )
  )
  val crashLogs: StateFlow<List<CrashLogEntry>> = _crashLogs.asStateFlow()

  // --- Actions & Methods ---

  suspend fun sendUserMessage(text: String) {
    val userMsg = ChatMessage(
      id = UUID.randomUUID().toString(),
      text = text,
      sender = MessageSender.USER
    )
    _chatMessages.value = _chatMessages.value + userMsg

    // Simulate agent thought and autonomous parsing
    delay(400)

    val lower = text.lowercase()
    val (replyText, toolActions, plan) = generateAgentResponse(lower, text)

    val agentMsg = ChatMessage(
      id = UUID.randomUUID().toString(),
      text = replyText,
      sender = MessageSender.AGENT,
      toolActions = toolActions,
      plan = plan
    )
    _chatMessages.value = _chatMessages.value + agentMsg
  }

  private fun generateAgentResponse(
    lower: String,
    original: String
  ): Triple<String, List<ToolActionItem>, PlanItem?> {
    return when {
      lower.contains("social") || lower.contains("post") || lower.contains("tweet") -> {
        val action = ToolActionItem(
          id = UUID.randomUUID().toString(),
          title = "Compose Multi-Platform Post",
          description = "Draft announcement post for X and LinkedIn",
          category = ActionCategory.SOCIAL,
          riskLevel = RiskLevel.LOW,
          status = ActionStatus.PENDING,
          parameters = mapOf("platforms" to "X, LinkedIn", "theme" to "AI Automation on Android")
        )
        Triple(
          "I have drafted a social media update and added it to your Content Composer queue. You can review the scheduled time or execute it right here.",
          listOf(action),
          null
        )
      }

      lower.contains("wifi") || lower.contains("bluetooth") || lower.contains("volume") || lower.contains("brightness") || lower.contains("dnd") -> {
        val action = ToolActionItem(
          id = UUID.randomUUID().toString(),
          title = "System Control Action",
          description = "Configure device hardware setting based on command: '$original'",
          category = ActionCategory.SYSTEM,
          riskLevel = RiskLevel.LOW,
          status = if (_autoApprovalMode.value != AutoApprovalMode.CONSERVATIVE) ActionStatus.COMPLETED else ActionStatus.PENDING,
          parameters = mapOf("command" to original),
          executionResult = if (_autoApprovalMode.value != AutoApprovalMode.CONSERVATIVE) "Executed via Android System Service" else null
        )
        Triple(
          if (_autoApprovalMode.value != AutoApprovalMode.CONSERVATIVE)
            "System action completed automatically via auto-approval policy!"
          else
            "I prepared the system action. Please tap confirm below to execute it.",
          listOf(action),
          null
        )
      }

      lower.contains("routine") || lower.contains("morning") || lower.contains("focus") -> {
        val action = ToolActionItem(
          id = UUID.randomUUID().toString(),
          title = "Trigger Routine: Morning Launchpad",
          description = "Run unread notification briefing, toggle DND, and retrieve calendar",
          category = ActionCategory.AUTOMATION,
          riskLevel = RiskLevel.MEDIUM,
          status = ActionStatus.PENDING,
          parameters = mapOf("routine" to "Morning Launchpad")
        )
        Triple(
          "I have staged your 'Morning Launchpad' routine. Here are the 3 actions queued for execution:",
          listOf(action),
          null
        )
      }

      lower.contains("plan") || lower.contains("launch") || lower.contains("campaign") || lower.contains("project") -> {
        val newPlan = PlanItem(
          id = UUID.randomUUID().toString(),
          title = "Autonomous Goal: " + original.take(40),
          goalDescription = original,
          status = PlanStatus.RUNNING,
          steps = listOf(
            PlanStepItem(
              stepIndex = 1,
              actionTitle = "Analyze Device State & Prerequisites",
              detail = "Verify permissions, active accounts and network status",
              status = PlanStepStatus.DONE,
              outputLog = "All prerequisites satisfied."
            ),
            PlanStepItem(
              stepIndex = 2,
              actionTitle = "Execute Core Automation Tasks",
              detail = "Call system and external service handlers",
              status = PlanStepStatus.RUNNING,
              outputLog = "Executing in background..."
            ),
            PlanStepItem(
              stepIndex = 3,
              actionTitle = "Log Audit & Update Memory Bank",
              detail = "Store learned patterns in personal knowledge graph",
              status = PlanStepStatus.PENDING
            )
          )
        )
        Triple(
          "I have decomposed your objective into a 3-step structured plan. I am currently executing Step 2.",
          emptyList(),
          newPlan
        )
      }

      else -> {
        Triple(
          "Understood! As an autonomous agent, I can analyze your request, formulate a multi-step plan, interface with device features (WiFi, volume, notifications, macros), manage your social presence, and record key insights to your memory bank.",
          listOf(
            ToolActionItem(
              id = UUID.randomUUID().toString(),
              title = "Knowledge Graph Query",
              description = "Retrieved contextual user preferences from local memory",
              category = ActionCategory.SYSTEM,
              riskLevel = RiskLevel.LOW,
              status = ActionStatus.COMPLETED,
              executionResult = "Matched 3 personal knowledge nodes"
            )
          ),
          null
        )
      }
    }
  }

  fun approveAction(actionId: String) {
    _chatMessages.value = _chatMessages.value.map { msg ->
      val updatedActions = msg.toolActions.map { action ->
        if (action.id == actionId) {
          action.copy(
            status = ActionStatus.COMPLETED,
            executionResult = "Successfully executed on Android device at ${System.currentTimeMillis() % 100000}"
          )
        } else action
      }
      msg.copy(toolActions = updatedActions)
    }
  }

  fun denyAction(actionId: String) {
    _chatMessages.value = _chatMessages.value.map { msg ->
      val updatedActions = msg.toolActions.map { action ->
        if (action.id == actionId) {
          action.copy(
            status = ActionStatus.DENIED,
            executionResult = "Cancelled by user"
          )
        } else action
      }
      msg.copy(toolActions = updatedActions)
    }
  }

  fun advancePlanStep(planId: String) {
    _plans.value = _plans.value.map { plan ->
      if (plan.id == planId) {
        val nextPendingIndex = plan.steps.indexOfFirst { it.status == PlanStepStatus.PENDING || it.status == PlanStepStatus.RUNNING }
        if (nextPendingIndex != -1) {
          val updatedSteps = plan.steps.mapIndexed { idx, step ->
            when (idx) {
              nextPendingIndex -> step.copy(status = PlanStepStatus.DONE, outputLog = "Completed successfully")
              nextPendingIndex + 1 -> step.copy(status = PlanStepStatus.RUNNING)
              else -> step
            }
          }
          val allDone = updatedSteps.all { it.status == PlanStepStatus.DONE }
          plan.copy(
            steps = updatedSteps,
            status = if (allDone) PlanStatus.COMPLETED else PlanStatus.RUNNING
          )
        } else plan
      } else plan
    }
  }

  fun createNewPlan(title: String, goal: String, stepTitles: List<String>) {
    val steps = stepTitles.mapIndexed { idx, st ->
      PlanStepItem(
        stepIndex = idx + 1,
        actionTitle = st,
        detail = "Action step for $st",
        status = if (idx == 0) PlanStepStatus.RUNNING else PlanStepStatus.PENDING
      )
    }
    val item = PlanItem(
      id = UUID.randomUUID().toString(),
      title = title,
      goalDescription = goal,
      steps = steps,
      status = PlanStatus.RUNNING
    )
    _plans.value = listOf(item) + _plans.value
  }

  fun toggleRoutine(id: String) {
    _routines.value = _routines.value.map {
      if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it
    }
  }

  fun addRoutine(name: String, triggerDesc: String, type: TriggerType, actions: List<String>) {
    val routine = RoutineItem(
      id = UUID.randomUUID().toString(),
      name = name,
      triggerDesc = triggerDesc,
      triggerType = type,
      actionsList = actions,
      isEnabled = true,
      lastRunTime = "Never"
    )
    _routines.value = _routines.value + routine
  }

  fun runMacro(id: String) {
    _macros.value = _macros.value.map {
      if (it.id == id) it.copy(runCount = it.runCount + 1) else it
    }
  }

  fun addMacro(name: String, app: String, stepsCount: Int, tags: List<String>) {
    val macro = MacroItem(
      id = UUID.randomUUID().toString(),
      name = name,
      stepsCount = stepsCount,
      recordedApp = app,
      tags = tags,
      runCount = 1,
      isFavorite = false
    )
    _macros.value = _macros.value + macro
  }

  fun publishSocialPost(content: String, platforms: List<SocialPlatformType>) {
    val newPosts = platforms.map { platform ->
      SocialPostItem(
        id = UUID.randomUUID().toString(),
        platform = platform,
        content = content,
        scheduledTime = "Just now",
        status = SocialPostStatus.PUBLISHED,
        likes = 1,
        shares = 0,
        comments = 0
      )
    }
    _socialPosts.value = newPosts + _socialPosts.value
  }

  fun toggleAccountConnection(accountId: String) {
    _socialAccounts.value = _socialAccounts.value.map {
      if (it.id == accountId) it.copy(isConnected = !it.isConnected) else it
    }
  }

  fun addMemory(subject: String, predicate: String, obj: String, category: MemoryCategory) {
    val node = MemoryNode(
      id = UUID.randomUUID().toString(),
      subject = subject,
      predicate = predicate,
      obj = obj,
      category = category,
      confidence = 0.99f,
      timestamp = "Just now"
    )
    _memories.value = listOf(node) + _memories.value
  }

  fun deleteMemory(id: String) {
    _memories.value = _memories.value.filter { it.id != id }
  }

  fun setActiveProvider(provider: LLMProviderType) {
    _activeProvider.value = provider
  }

  fun setAutoApprovalMode(mode: AutoApprovalMode) {
    _autoApprovalMode.value = mode
  }

  fun setTemperature(temp: Float) {
    _temperature.value = temp
  }

  suspend fun downloadOnDeviceModel(modelId: String) {
    val target = _onDeviceModels.value.find { it.id == modelId } ?: return
    if (target.isDownloaded) return

    for (p in 1..10) {
      delay(150)
      _onDeviceModels.value = _onDeviceModels.value.map {
        if (it.id == modelId) it.copy(downloadProgress = p / 10f) else it
      }
    }

    _onDeviceModels.value = _onDeviceModels.value.map {
      if (it.id == modelId) it.copy(isDownloaded = true, downloadProgress = 1f) else it
    }
  }

  suspend fun runBenchmark(modelName: String) {
    delay(800)
    _lastBenchmark.value = BenchmarkResult(
      modelTested = modelName,
      firstTokenLatencyMs = (180..260).random().toLong(),
      tokensPerSec = (24..35).random() + 0.6f,
      ramUsageMb = (750..950).random(),
      temperatureCelsius = 36.8f + (1..15).random() / 10f
    )
  }

  fun clearChat() {
    _chatMessages.value = emptyList()
  }
}
