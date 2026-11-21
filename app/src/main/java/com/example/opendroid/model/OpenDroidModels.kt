package com.example.opendroid.model

enum class MessageSender {
  USER,
  AGENT,
  SYSTEM
}

enum class ActionCategory {
  SYSTEM,
  COMMUNICATION,
  MEDIA,
  SMART_HOME,
  SOCIAL,
  FINANCE,
  CALENDAR,
  AUTOMATION
}

enum class RiskLevel {
  LOW,       // Safe to auto-execute in balanced/autonomous mode
  MEDIUM,    // Requires confirmation or balanced auto-approval
  HIGH       // Always prompts user confirmation before executing
}

enum class ActionStatus {
  PENDING,
  APPROVED,
  EXECUTING,
  COMPLETED,
  DENIED
}

data class ToolActionItem(
  val id: String,
  val title: String,
  val description: String,
  val category: ActionCategory,
  val riskLevel: RiskLevel,
  val status: ActionStatus = ActionStatus.PENDING,
  val parameters: Map<String, String> = emptyMap(),
  val executionResult: String? = null
)

enum class PlanStepStatus {
  PENDING,
  RUNNING,
  DONE,
  FAILED
}

data class PlanStepItem(
  val stepIndex: Int,
  val actionTitle: String,
  val detail: String,
  val isOptional: Boolean = false,
  val status: PlanStepStatus = PlanStepStatus.PENDING,
  val outputLog: String? = null
)

enum class PlanStatus {
  DRAFT,
  RUNNING,
  COMPLETED,
  PAUSED,
  FAILED
}

data class PlanItem(
  val id: String,
  val title: String,
  val goalDescription: String,
  val steps: List<PlanStepItem>,
  val status: PlanStatus = PlanStatus.DRAFT,
  val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
  val id: String,
  val text: String,
  val sender: MessageSender,
  val timestamp: Long = System.currentTimeMillis(),
  val toolActions: List<ToolActionItem> = emptyList(),
  val plan: PlanItem? = null,
  val isStreaming: Boolean = false,
  val error: String? = null
)

enum class TriggerType {
  TIME,
  BATTERY,
  WIFI,
  HEADPHONES,
  NOTIFICATION,
  LOCATION
}

data class RoutineItem(
  val id: String,
  val name: String,
  val triggerDesc: String,
  val triggerType: TriggerType,
  val actionsList: List<String>,
  val isEnabled: Boolean,
  val lastRunTime: String
)

data class MacroItem(
  val id: String,
  val name: String,
  val stepsCount: Int,
  val recordedApp: String,
  val tags: List<String>,
  val runCount: Int = 0,
  val isFavorite: Boolean = false
)

enum class SocialPlatformType {
  X,
  LINKEDIN,
  INSTAGRAM,
  FACEBOOK,
  YOUTUBE,
  DISCORD,
  TELEGRAM
}

enum class SocialPostStatus {
  DRAFT,
  SCHEDULED,
  PUBLISHED,
  FAILED
}

data class SocialPostItem(
  val id: String,
  val platform: SocialPlatformType,
  val content: String,
  val scheduledTime: String?,
  val status: SocialPostStatus = SocialPostStatus.DRAFT,
  val likes: Int = 0,
  val shares: Int = 0,
  val comments: Int = 0
)

data class SocialAccount(
  val id: String,
  val platform: SocialPlatformType,
  val handle: String,
  val isConnected: Boolean,
  val followerCount: String,
  val avatarColorHex: Long
)

enum class MemoryCategory {
  PREFERENCES,
  WORK,
  PERSONAL,
  HABITS,
  TECH
}

data class MemoryNode(
  val id: String,
  val subject: String,
  val predicate: String,
  val obj: String,
  val category: MemoryCategory,
  val confidence: Float = 0.95f,
  val timestamp: String = "Today"
)

enum class LLMProviderType(val displayName: String, val badge: String) {
  GEMINI("Google Gemini (AI Studio)", "Fast & Multimodal"),
  OPENAI("OpenAI (GPT-4o)", "Reasoning"),
  CLAUDE("Anthropic Claude", "Coding"),
  GROQ("Groq LPUs", "Ultra Fast"),
  DEEPSEEK("DeepSeek", "Efficient"),
  MISTRAL("Mistral AI", "Compact"),
  OLLAMA("Ollama (Local Server)", "Private Local"),
  LITERTLM("LiteRT / Gemma On-Device", "100% Offline")
}

enum class AutoApprovalMode(val title: String, val description: String) {
  CONSERVATIVE("Conservative (High Safety)", "Prompts user for medium and high risk actions"),
  BALANCED("Balanced (Recommended)", "Auto-approves low & medium risk system controls"),
  AUTONOMOUS("Full Autonomous", "Executes verified plans automatically with live audit log")
}

data class OnDeviceModelInfo(
  val id: String,
  val name: String,
  val size: String,
  val parameters: String,
  val isDownloaded: Boolean,
  val downloadProgress: Float = 0f,
  val isQuantized: Boolean = true
)

data class BenchmarkResult(
  val modelTested: String,
  val firstTokenLatencyMs: Long,
  val tokensPerSec: Float,
  val ramUsageMb: Int,
  val temperatureCelsius: Float
)

data class CrashLogEntry(
  val id: String,
  val timestamp: String,
  val tag: String,
  val message: String,
  val stackTrace: String,
  val isFatal: Boolean = false
)
