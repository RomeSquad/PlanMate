package org.example.data.datasource.changelog

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.example.logic.entity.ChangeHistory

class MongoChangeHistoryDataSource(
    val mongo: MongoCollection<ChangeHistory>
) : ChangeHistoryDataSource {

    override suspend fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory {
        return mongo.insertOne(changeHistory).let { changeHistory }
    }

    override suspend fun getByProjectId(projectId: Int): List<ChangeHistory> {
        val filter = Filters.eq("projectID", projectId)
        return mongo.find(filter).toList()
    }

    override suspend fun getByTaskId(taskId: Int): List<ChangeHistory> {
        val filter = Filters.eq("taskID", taskId)
        return mongo.find(filter).toList()
    }
}