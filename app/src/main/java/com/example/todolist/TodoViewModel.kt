package com.example.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val db = TodoDatabase.getDatabase(application)
    private val dao = db.todoDao()

    val allTodos = dao.getAllTodos()

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            dao.insert(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            dao.delete(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            dao.update(todo)
        }
    }
}
