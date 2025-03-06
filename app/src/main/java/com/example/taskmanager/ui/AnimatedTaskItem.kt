import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import com.example.taskmanager.model.Task
import com.example.taskmanager.ui.TaskItem

@Composable
fun AnimatedTaskItem(task: Task, onTaskClicked: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        TaskItem(task = task, onTaskClicked = onTaskClicked)
    }
}