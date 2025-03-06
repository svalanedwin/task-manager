import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.model.Task

@Composable
fun AnimatedTaskItem(task: Task, onClick: (Task) -> Unit) {
    val animatedAlpha = remember { Animatable(0f) }

    // Launch animation only when the task item is first composed
    LaunchedEffect(task.id) {
        animatedAlpha.animateTo(1f, animationSpec = tween(durationMillis = 500))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .graphicsLayer(alpha = animatedAlpha.value) // ✅ FIXED Modifier
            .clickable { onClick(task) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp) // ✅ FIXED Elevation
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(task.title, fontWeight = FontWeight.Bold)
            task.description?.let { Text(it, fontSize = 14.sp, color = Color.Gray) }
        }
    }
}
