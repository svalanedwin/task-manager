import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import com.example.taskmanager.model.Task
import com.example.taskmanager.ui.TaskItem
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun AnimatedTaskItem(task: Task,viewModel: TaskViewModel ,onTaskClicked: () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(),
        exit = slideOutHorizontally()
    ) {
        TaskItem(
            task = task, onTaskClicked = onTaskClicked,
            viewModel = viewModel
        )
    }
}