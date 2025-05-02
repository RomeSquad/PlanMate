package org.example.data.datasource.task

import data.utils.CustomFile
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.data.utils.fromCsvRowToTask
import org.example.data.utils.toCsvRow
import org.example.logic.entity.Task

class CsvTaskDataSource (
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter
): TaskDataSource {

    private val tasksFile = CustomFile("tasks.csv")

    override fun getTaskByIdFromFile(taskId: String): Result<Task> {
        return try {
            val data = csvFileReader.readCsv(tasksFile)
            val tasks = data.map { it.fromCsvRowToTask() }
            val task = tasks.firstOrNull { it.id == taskId }
                ?: return Result.failure(NoSuchElementException("Not founded for this id"))
            Result.success(task)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun getAllTasks(): Result<List<Task>> {
        val data = csvFileReader.readCsv(tasksFile)
        println(data[0])
        val tasks = data.map { it.fromCsvRowToTask() }
        return Result.success(tasks)
    }

    override fun setAllTasks(tasks: List<Task>): Result<Unit> {
        tasks.forEach { task ->
            val row = task.toCsvRow()
            csvFileWriter.writeCsv(tasksFile, listOf(row))
        }
        return Result.success(Unit)
    }

    override fun createTask(task: Task): Result<Unit> {
        return try {
            val row = task.toCsvRow()
            csvFileWriter.writeCsv(tasksFile, listOf(row))
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun deleteTask(projectId: Int, taskId: String) {
        val result = getAllTasks()
        if (result.isFailure) throw result.exceptionOrNull()!!

        val allTasks = result.getOrThrow().toMutableList()
        val removed = allTasks.removeIf { it.projectId == projectId && it.id == taskId }

        if (!removed) {
            throw NoSuchElementException("Task with id $taskId in project $projectId not found")
        }

        val writeResult = setAllTasks(allTasks)
        if (writeResult.isFailure) throw writeResult.exceptionOrNull()!!
    }
}