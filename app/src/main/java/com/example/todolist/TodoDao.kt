package com.example.todolist

import androidx.room.*

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo_table ORDER BY id DESC")
    fun getAllTodos(): kotlinx.coroutines.flow.Flow<List<Todo>>

    @Update
    suspend fun update(todo: Todo)
}
