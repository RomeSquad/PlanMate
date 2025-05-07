package org.example.data.datasource.changelog

import org.example.logic.entity.ChangeHistory

class MongoChangeHistoryDataSource(
    val mongo: MongoCollection<ChangeHistory>
) : ChangeHistoryDataSource {

    override fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory {
        return mongo.insertOne(changeHistory)
    }

    override fun getByProjectId(projectId: Int): List<ChangeHistory> {
        return mongo.find(projectID == projectId)
    }

    override fun getByTaskId(taskId: Int): List<ChangeHistory> {
        return mongo.finf(taskID == taskId)
    }
}