package org.example.data.datasource.task

import org.example.data.datasource.mapper.fromCsvRowToTask
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import java.io.File

class CsvTaskDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val taskFile: File
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        val row = task.toCsvRow()
        csvFileWriter.writeCsv(taskFile, listOf(row))
    }

    override suspend fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        val tasks = getAllTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index == -1) {
            throw TaskNotFoundException("Task with id $taskId not found")
        }

        val task = tasks[index]
        tasks[index] = task.copy(
            title = title,
            description = description,
            updatedAt = updatedAt
        )

        saveAllTasks(tasks)
    }

    override suspend fun deleteTask(projectId: Int, taskId: String) {
        val allTasks = getAllTasks().toMutableList()
        val removed = allTasks.removeIf {
            it.projectId == projectId
                    &&
                    it.id == taskId
        }

        if (!removed) {
            throw TaskNotFoundException(
                "Task with id $taskId in project $projectId not found"
            )
        }

        saveAllTasks(allTasks)
    }

    override suspend fun getTaskByIdFromFile(taskId: String): Task {
        val data = csvFileReader.readCsv(taskFile)
        val tasks = data.map { it.fromCsvRowToTask() }
        val task = tasks.firstOrNull { it.id == taskId }
            ?: throw TaskNotFoundException("Task not found for this id")
        return task
    }

    override suspend fun getTasksByProjectId(projectId: Int): List<Task> {
        return getAllTasks().filter { it.projectId == projectId }
    }

    override suspend fun getAllTasks(): List<Task> {
        val data = csvFileReader.readCsv(taskFile)
        return data.map { it.fromCsvRowToTask() }
    }

    override suspend fun saveAllTasks(tasks: List<Task>) {
        tasks.forEach { task ->
            val row = task.toCsvRow()
            csvFileWriter.writeCsv(taskFile, listOf(row))
        }
    }
}