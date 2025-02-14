package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.theme.TodolistTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodolistTheme {
                Scaffold(
                    topBar = {TopAppBar(title = { Text("Todo List") })},
                    modifier = Modifier.fillMaxSize(),
                ){ innerPadding->
                    TodoApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TodoApp(todoViewModel: TodoViewModel = viewModel(), modifier: Modifier) {
    val todos by todoViewModel.allTodos.collectAsState(initial = emptyList())
    var text by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()
        .padding(16.dp)) {
        LazyColumn {
            items(todos, key = {it.id}) { todo ->
                TodoItem(
                    todo = todo,
                    onDelete = { todoViewModel.deleteTodo(todo) },
                    onEdit = {updatedTodo -> todoViewModel.updateTodo(updatedTodo)})
            }
        }
        Spacer(modifier = Modifier.weight(2f))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("New Task") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (text.isNotBlank()) {
                    todoViewModel.addTodo(Todo(title = text))
                    text = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onDelete: () -> Unit, onEdit: (Todo) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(todo.title) }
    var checked by remember { mutableStateOf( todo.isCompleted) }

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        elevation = elevatedCardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (isEditing) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                onEdit(todo.copy(title = text))
                                isEditing = false
                            }
                        }
                    ) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            isEditing = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            onEdit(todo.copy(isCompleted = it))}
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(todo.title)
                    Spacer(modifier = Modifier.weight(2f))
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                    }
                }
            }
        }
    }
}