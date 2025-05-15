package org.example.data.datasource.task

import org.example.data.datasource.mapper.fromCsvRowToTask
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import org.example.logic.request.TaskDeletionRequest
import org.example.logic.request.TaskEditRequest
import java.io.File
import java.util.*

class CsvTaskDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val taskFile: File
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        val row = task.toCsvRow()
        csvFileWriter.writeCsv(taskFile, listOf(row))
    }

    override suspend fun editTask(request: TaskEditRequest) {
        val tasks = getAllTasks().toMutableList()
        val index = tasks.indexOfFirst { it.taskId == request.taskId }

        if (index == -1) {
            throw TaskNotFoundException("Task with id ${request.taskId} not found")
        }

        val task = tasks[index]
        tasks[index] = task.copy(
            title = request.title,
            description = request.description,
            updatedAt = request.updatedAt
        )

        saveAllTasks(tasks)
    }

    override suspend fun deleteTask(request: TaskDeletionRequest) {
        val allTasks = getAllTasks().toMutableList()
        val removed = allTasks.removeIf {
            it.projectId == request.projectId
                    &&
                    it.taskId == request.taskId
        }

        if (!removed) {
            throw TaskNotFoundException(
                "Task with id ${request.taskId} in project {$request.projectId} not found"
            )
        }

        saveAllTasks(allTasks)
    }

    override suspend fun getTaskByIdFromFile(taskId: UUID): Task {
        val data = csvFileReader.readCsv(taskFile)
        val tasks = data.map { it.fromCsvRowToTask() }
        val task = tasks.firstOrNull { it.taskId == taskId }
            ?: throw TaskNotFoundException("Task not found for this id")
        return task
    }

    override suspend fun getTasksByProjectId(projectId: UUID): List<Task> {
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