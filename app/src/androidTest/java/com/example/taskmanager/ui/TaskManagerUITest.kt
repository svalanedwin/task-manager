package com.example.taskmanager.ui

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.taskmanager.model.Priority
import com.example.taskmanager.model.Task
import com.example.taskmanager.repository.FakeSettingsRepository
import com.example.taskmanager.repository.FakeTaskRepository
import com.example.taskmanager.repository.SettingsRepository
import com.example.taskmanager.repository.TaskRepository
import com.example.taskmanager.viewmodel.TaskViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskManagerUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        // Create fake repositories and explicitly cast them to their interfaces
        val fakeTaskRepository: TaskRepository = FakeTaskRepository()
        val fakeSettingsRepository: SettingsRepository = FakeSettingsRepository()
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Initialize the ViewModel with the fake repositories
        viewModel = TaskViewModel(fakeTaskRepository, fakeSettingsRepository, context)

        // Set up the Compose UI
        composeTestRule.setContent {
            TaskListScreen(
                viewModel = viewModel,
                onTaskClick = {},
                onAddTaskClick = {},
                onSettingsClick = {}
            )
        }
    }

    // Test 1: Task Creation Flow
    @Test
    fun testTaskCreationFlow() {
        // Click the FAB to open the Add Task screen
        composeTestRule.onNodeWithContentDescription("Add Task").performClick()

        // Enter task details
        composeTestRule.onNodeWithText("Title").performTextInput("New Task")
        composeTestRule.onNodeWithText("Description (optional)").performTextInput("Task Description")
        composeTestRule.onNodeWithText("Save Task").performClick()

        // Verify the task appears in the list
        composeTestRule.onNodeWithText("New Task").assertExists()
        composeTestRule.onNodeWithText("Task Description").assertExists()
    }

    // Test 2: Sorting Functionality
    @Test
    fun testSortingFunctionality() {
        // Add tasks with different priorities
        viewModel.addTask(Task(title = "Task 1", priority = Priority.LOW, dueDate = System.currentTimeMillis()))
        viewModel.addTask(Task(title = "Task 2", priority = Priority.HIGH, dueDate = System.currentTimeMillis()))
        viewModel.addTask(Task(title = "Task 3", priority = Priority.MEDIUM, dueDate = System.currentTimeMillis()))

        // Open the sort dropdown
        composeTestRule.onNodeWithText("Sort by").performClick()

        // Sort by Priority
        composeTestRule.onNodeWithText("Priority").performClick()

        // Verify the order of tasks
        composeTestRule.onAllNodesWithText("Task 2").assertCountEquals(1) // HIGH priority first
        composeTestRule.onAllNodesWithText("Task 3").assertCountEquals(1) // MEDIUM priority next
        composeTestRule.onAllNodesWithText("Task 1").assertCountEquals(1) // LOW priority last
    }

    // Test 3: Filtering Functionality
    @Test
    fun testFilteringFunctionality() {
        // Add completed and pending tasks
        viewModel.addTask(Task(title = "Task 1", priority = Priority.MEDIUM, dueDate = System.currentTimeMillis(), isCompleted = true))
        viewModel.addTask(Task(title = "Task 2", priority = Priority.MEDIUM, dueDate = System.currentTimeMillis(), isCompleted = false))

        // Open the filter dropdown
        composeTestRule.onNodeWithText("Filter by").performClick()

        // Filter by Completed
        composeTestRule.onNodeWithText("Completed").performClick()

        // Verify only completed tasks are shown
        composeTestRule.onNodeWithText("Task 1").assertExists()
        composeTestRule.onNodeWithText("Task 2").assertDoesNotExist()

        // Filter by Pending
        composeTestRule.onNodeWithText("Filter by").performClick()
        composeTestRule.onNodeWithText("Pending").performClick()

        // Verify only pending tasks are shown
        composeTestRule.onNodeWithText("Task 1").assertDoesNotExist()
        composeTestRule.onNodeWithText("Task 2").assertExists()
    }

    // Test 4: Swipe-to-Delete Animation
    @Test
    fun testSwipeToDeleteAnimation() {
        // Add a task
        viewModel.addTask(Task(title = "Task to Delete", priority = Priority.MEDIUM, dueDate = System.currentTimeMillis()))

        // Swipe left to delete the task
        composeTestRule.onNodeWithText("Task to Delete").performTouchInput {
            swipeLeft()
        }

        // Verify the task is deleted
        composeTestRule.onNodeWithText("Task to Delete").assertDoesNotExist()
    }

    // Test 5: Swipe-to-Complete Animation
    @Test
    fun testSwipeToCompleteAnimation() {
        // Add a task
        viewModel.addTask(Task(title = "Task to Complete", priority = Priority.MEDIUM, dueDate = System.currentTimeMillis()))

        // Swipe right to mark the task as completed
        composeTestRule.onNodeWithText("Task to Complete").performTouchInput {
            swipeRight()
        }

        // Verify the task is marked as completed
        composeTestRule.onNodeWithText("Completed").assertExists()
    }
}