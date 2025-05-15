package org.example.data.datasource.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.example.data.utils.TaskConstants.PROJECT_ID
import org.example.data.utils.TaskConstants.TASK_DESCRIPTION
import org.example.data.utils.TaskConstants.TASK_ID
import org.example.data.utils.TaskConstants.TASK_TITLE
import org.example.data.utils.TaskConstants.TASK_UPDATED_AT
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import org.example.logic.request.auth.TaskDeletionRequest
import org.example.logic.request.auth.TaskEditRequest
import java.util.*

class MongoTaskDataSource(
    private val mongo: MongoCollection<Task>
) : TaskDataSource {

    override suspend fun createTask(task: Task) {
        mongo.insertOne(task)
    }

    override suspend fun editTask(request : TaskEditRequest) {
        val update = Updates.combine(
            Updates.set(TASK_TITLE, request.title),
            Updates.set(TASK_DESCRIPTION, request.description),
            Updates.set(TASK_UPDATED_AT, request.updatedAt)
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
            Filters.eq(TASK_ID, request.taskId),
            Filters.eq(PROJECT_ID, request.projectId)
        )

        mongo.deleteOne(filter)
    }

    override suspend fun getTaskByIdFromFile(taskId: UUID): Task {
        return mongo.find(Filters.eq(TASK_ID, taskId)).first()
    }

    override suspend fun getTasksByProjectId(projectId: UUID): List<Task> {
        return mongo.find(Filters.eq(PROJECT_ID, projectId)).toList()
    }

    override suspend fun getAllTasks(): List<Task> {
        return mongo.find().toList()
    }

    override suspend fun saveAllTasks(tasks: List<Task>) {
        mongo.insertMany(tasks)
    }
}