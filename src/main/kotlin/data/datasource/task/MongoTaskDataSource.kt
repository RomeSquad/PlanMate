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
import java.util.UUID

class MongoTaskDataSource(
    val mongo: MongoCollection<Task>
) : TaskDataSource {
    override suspend fun createTask(task: Task) {
        mongo.insertOne(task)
    }

    override suspend fun editTask(
        taskId: UUID,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        val update = Updates.combine(
            Updates.set(TASK_TITLE, title),
            Updates.set(TASK_DESCRIPTION, description),
            Updates.set(TASK_UPDATED_AT, updatedAt)
        )

        val editedTask = mongo.updateOne(
            filter = Filters.eq(TASK_ID, taskId),
            update = update
        )

        if (editedTask.matchedCount == 0L) {
            throw TaskNotFoundException(
                "Task with id $taskId not found"
            )
        }
    }

    override suspend fun deleteTask(projectId: UUID, taskId: UUID) {
        val filter = Filters.and(
            Filters.eq(TASK_ID, taskId),
            Filters.eq(PROJECT_ID, projectId)
        )
        val deleteResult = mongo.deleteOne(filter)

        if (deleteResult.deletedCount == 0L) {
            throw TaskNotFoundException(
                "Task with id $taskId in project $projectId not found"
            )
        }
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