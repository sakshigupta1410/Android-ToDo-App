package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editTask: EditText
    private lateinit var editStatus: EditText
    private lateinit var editDeadline: EditText
    private lateinit var addTaskButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private var editingTaskIndex: Int? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("TodoApp", Context.MODE_PRIVATE)

        editTask = findViewById(R.id.editTask)
        editStatus = findViewById(R.id.editStatus)
        editDeadline = findViewById(R.id.editDeadline)
        addTaskButton = findViewById(R.id.addTaskButton)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList, ::editTask, ::deleteTask)
        recyclerView.adapter = taskAdapter

        editDeadline.setOnClickListener { showDateTimePicker() }
        addTaskButton.setOnClickListener { saveTask() }

        loadTasks()  // Load saved tasks on app startup
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            TimePickerDialog(this, { _, hour, minute ->
                calendar.set(year, month, dayOfMonth, hour, minute)
                val formattedDateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(calendar.time)
                editDeadline.setText(formattedDateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun saveTask() {
        val taskText = editTask.text.toString().trim()
        val statusText = editStatus.text.toString().trim()
        val deadlineText = editDeadline.text.toString().trim()

        if (taskText.isNotEmpty() && statusText.isNotEmpty() && deadlineText.isNotEmpty()) {
            if (editingTaskIndex == null) {
                taskList.add(Task(taskText, statusText, deadlineText))
            } else {
                taskList[editingTaskIndex!!] = Task(taskText, statusText, deadlineText)
                editingTaskIndex = null
            }

            saveTasksToStorage()
            taskAdapter.notifyDataSetChanged()
            clearFields()
        }
    }

    private fun editTask(position: Int) {
        val task = taskList[position]
        editTask.setText(task.name)
        editStatus.setText(task.status)
        editDeadline.setText(task.deadline)
        editingTaskIndex = position
    }

    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        saveTasksToStorage()
        taskAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        editTask.text.clear()
        editStatus.text.clear()
        editDeadline.text.clear()
    }

    private fun saveTasksToStorage() {
        val jsonString = gson.toJson(taskList)
        sharedPreferences.edit().putString("tasks", jsonString).apply()
    }

    private fun loadTasks() {
        val jsonString = sharedPreferences.getString("tasks", null)
        if (jsonString != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            taskList.clear()
            taskList.addAll(gson.fromJson(jsonString, type))
            taskAdapter.notifyDataSetChanged()
        }
    }
}

