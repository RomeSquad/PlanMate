package org.example.data.datasource.task

import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.data.repository.mapper.fromCsvRowToTask
import org.example.data.repository.mapper.toCsvRow
import org.example.logic.entity.Task
import java.io.File

class CsvTaskDataSource (
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val taskFile: File
): TaskDataSource {

    override fun getTaskByIdFromFile(taskId: String): Result<Task> {
        return try {
            val data = csvFileReader.readCsv(taskFile)
            val tasks = data.map { it.fromCsvRowToTask() }
            val task = tasks.firstOrNull { it.id == taskId }
                ?: return Result.failure(NoSuchElementException("Not founded for this id"))
            Result.success(task)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun getAllTasks(): Result<List<Task>> {
        val data = csvFileReader.readCsv(taskFile)
        println(data[0])
        val tasks = data.map { it.fromCsvRowToTask() }
        return Result.success(tasks)
    }

    override fun setAllTasks(tasks: List<Task>): Result<Unit> {
        tasks.forEach { task ->
            val row = task.toCsvRow()
            csvFileWriter.writeCsv(taskFile, listOf(row))
        }
        return Result.success(Unit)
    }

    override fun createTask(task: Task): Result<Unit> {
        return try {
            val row = task.toCsvRow()
            csvFileWriter.writeCsv(taskFile, listOf(row))
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