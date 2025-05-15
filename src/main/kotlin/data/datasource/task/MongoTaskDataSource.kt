package org.example.data.datasource.task

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import java.util.*

class MongoTaskDataSource(
    private val mongo: MongoCollection<Task>
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
            Updates.set(Task::title.name, title),
            Updates.set(Task::description.name, description),
            Updates.set(Task::updatedAt.name, updatedAt)
        )

        val editedTask = mongo.updateOne(
            filter = Filters.eq(Task::taskId.name, taskId),
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
            Filters.eq(Task::taskId.name, taskId),
            Filters.eq(Task::projectId.name, projectId)
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