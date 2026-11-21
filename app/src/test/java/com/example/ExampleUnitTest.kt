package com.example

import com.example.opendroid.data.OpenDroidRepository
import com.example.opendroid.model.ActionStatus
import com.example.opendroid.model.AutoApprovalMode
import com.example.opendroid.model.LLMProviderType
import com.example.opendroid.model.MemoryCategory
import com.example.opendroid.model.PlanStatus
import com.example.opendroid.model.PlanStepStatus
import com.example.opendroid.model.SocialPlatformType
import com.example.opendroid.model.TriggerType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun openDroidRepository_initialState_isLoaded() {
    val repo = OpenDroidRepository()
    assertTrue(repo.chatMessages.value.isNotEmpty())
    assertTrue(repo.routines.value.isNotEmpty())
    assertTrue(repo.macros.value.isNotEmpty())
    assertTrue(repo.plans.value.isNotEmpty())
    assertTrue(repo.socialAccounts.value.isNotEmpty())
    assertTrue(repo.memories.value.isNotEmpty())
    assertEquals(LLMProviderType.GEMINI, repo.activeProvider.value)
    assertEquals(AutoApprovalMode.BALANCED, repo.autoApprovalMode.value)
  }

  @Test
  fun openDroidRepository_sendUserMessage_generatesResponse() = runBlocking {
    val repo = OpenDroidRepository()
    val initialCount = repo.chatMessages.value.size
    repo.sendUserMessage("Please turn off WiFi")
    assertEquals(initialCount + 2, repo.chatMessages.value.size)
  }

  @Test
  fun openDroidRepository_planManagement_worksCorrectly() {
    val repo = OpenDroidRepository()
    val initialPlanCount = repo.plans.value.size
    repo.createNewPlan("Test Campaign", "Test Goal", listOf("Step A", "Step B"))
    assertEquals(initialPlanCount + 1, repo.plans.value.size)

    val createdPlan = repo.plans.value.first()
    assertEquals("Test Campaign", createdPlan.title)
    assertEquals(PlanStepStatus.RUNNING, createdPlan.steps.first().status)

    repo.advancePlanStep(createdPlan.id)
    val advancedPlan = repo.plans.value.first { it.id == createdPlan.id }
    assertEquals(PlanStepStatus.DONE, advancedPlan.steps.first().status)
  }

  @Test
  fun openDroidRepository_routinesAndMacros_toggleAndExecute() {
    val repo = OpenDroidRepository()
    val routineId = repo.routines.value.first().id
    val initialEnabled = repo.routines.value.first().isEnabled

    repo.toggleRoutine(routineId)
    assertEquals(!initialEnabled, repo.routines.value.first { it.id == routineId }.isEnabled)

    val macroId = repo.macros.value.first().id
    val initialRunCount = repo.macros.value.first().runCount
    repo.runMacro(macroId)
    assertEquals(initialRunCount + 1, repo.macros.value.first { it.id == macroId }.runCount)
  }

  @Test
  fun openDroidRepository_memoryGraph_addAndDelete() {
    val repo = OpenDroidRepository()
    val initialCount = repo.memories.value.size
    repo.addMemory("Assistant", "supports", "On-device LiteRT", MemoryCategory.TECH)
    assertEquals(initialCount + 1, repo.memories.value.size)

    val added = repo.memories.value.first()
    assertEquals("Assistant", added.subject)
    repo.deleteMemory(added.id)
    assertEquals(initialCount, repo.memories.value.size)
  }

  @Test
  fun openDroidRepository_settings_updateSuccessfully() {
    val repo = OpenDroidRepository()
    repo.setActiveProvider(LLMProviderType.LITERTLM)
    assertEquals(LLMProviderType.LITERTLM, repo.activeProvider.value)

    repo.setAutoApprovalMode(AutoApprovalMode.AUTONOMOUS)
    assertEquals(AutoApprovalMode.AUTONOMOUS, repo.autoApprovalMode.value)

    repo.setTemperature(0.9f)
    assertEquals(0.9f, repo.temperature.value, 0.01f)
  }
}
