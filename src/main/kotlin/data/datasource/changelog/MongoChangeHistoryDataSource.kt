package org.example.data.datasource.changelog

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.example.data.utils.data.utils.HistoryConstants.PROJECT_ID
import org.example.data.utils.data.utils.HistoryConstants.TASK_ID
import org.example.logic.entity.ModificationLog
import java.util.*

class MongoChangeHistoryDataSource(
    private val mongo: MongoCollection<ModificationLog	>
) : ChangeHistoryDataSource {

    override suspend fun addChangeHistory(changeHistory: ModificationLog	): ModificationLog	 {
        return mongo.insertOne(changeHistory).let { changeHistory }
    }

    override suspend fun getByProjectId(projectId: UUID): List<ModificationLog	> {
        val filter = Filters.eq(PROJECT_ID, projectId)
        return mongo.find(filter).toList()
    }

    override suspend fun getByTaskId(taskId: UUID): List<ModificationLog	> {
        val filter = Filters.eq(TASK_ID, taskId)
        return mongo.find(filter).toList()
    }
}