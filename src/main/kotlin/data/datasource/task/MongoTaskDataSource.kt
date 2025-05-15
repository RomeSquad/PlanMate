package org.example.data.datasource.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import org.example.logic.request.TaskDeletionRequest
import org.example.logic.request.TaskEditRequest
import java.util.*

class MongoTaskDataSource(
    private val mongo: MongoCollection<Task>
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        mongo.insertOne(task)
    }

    override suspend fun editTask(request : TaskEditRequest) {
        val update = Updates.combine(
            Updates.set(Task::title.name, request.title),
            Updates.set(Task::description.name, request.description),
            Updates.set(Task::updatedAt.name, request.updatedAt)
        )

        val editedTask = mongo.updateOne(
            filter = Filters.eq(TASK_ID, request.taskId),
            update = update
        )

        if (editedTask.matchedCount == 0L) {
            throw TaskNotFoundException(
                "Task with id ${request.taskId} not found"
            )
        }
    }

    override suspend fun deleteTask(request: TaskDeletionRequest) {
        val filter = Filters.and(
            Filters.eq(Task::taskId.name, request.taskId),
            Filters.eq(Task::projectId.name, request.projectId)
        )

        mongo.deleteOne(filter)
    }

    override suspend fun getTaskByIdFromFile(taskId: UUID): Task {
        return mongo.find(Filters.eq(Task::taskId.name, taskId)).first()
    }

    override suspend fun getTasksByProjectId(projectId: UUID): List<Task> {
        return mongo.find(Filters.eq(Task::projectId.name, projectId)).toList()
    }

    override suspend fun getAllTasks(): List<Task> {
        return mongo.find().toList()
    }

    override suspend fun saveAllTasks(tasks: List<Task>) {
        mongo.insertMany(tasks)
    }
}