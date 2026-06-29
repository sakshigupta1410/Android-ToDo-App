import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = taskList.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
        private val taskStatusTextView: TextView = itemView.findViewById(R.id.taskStatusTextView)
        private val taskDeadlineTextView: TextView = itemView.findViewById(R.id.taskDeadlineTextView)
        private val editTaskButton: Button = itemView.findViewById(R.id.editTaskButton)
        private val deleteTaskButton: Button = itemView.findViewById(R.id.deleteTaskButton)

        fun bind(task: Task) {
            taskNameTextView.text = task.name
            taskStatusTextView.text = task.status
            taskDeadlineTextView.text = task.deadline

            editTaskButton.setOnClickListener {
                // Edit task logic here
            }

            deleteTaskButton.setOnClickListener {
                // Delete task logic here
            }
        }
    }
}
