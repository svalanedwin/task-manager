import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.taskmanager.Priority
import com.example.taskmanager.Task
import com.example.taskmanager.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(viewModel: TaskViewModel, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Task") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            Button(onClick = {
                val task = Task(title = title, description = description, priority = Priority.MEDIUM, dueDate = System.currentTimeMillis())
                viewModel.insert(task)
                navController.popBackStack()
            }) {
                Text("Save")
            }
        }
    }
}