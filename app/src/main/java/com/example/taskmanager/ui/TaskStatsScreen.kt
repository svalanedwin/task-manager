import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.viewmodel.TaskViewModel

@Composable
fun TaskStatsScreen(viewModel: TaskViewModel) {
    val stats by viewModel.getTaskCompletionStats().observeAsState(TaskStats(0, 0))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Task Completion",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        CircularProgressIndicator(
            progress = { if (stats.total > 0) stats.completed.toFloat() / stats.total else 0f },
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${stats.completed} / ${stats.total} Tasks Completed",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
