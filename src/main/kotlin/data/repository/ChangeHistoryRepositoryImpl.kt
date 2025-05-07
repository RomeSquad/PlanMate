package org.example.data.repository

import org.example.data.datasource.changelog.ChangeHistoryDataSource
import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository

class ChangeHistoryRepositoryImpl(
    private val dataSource: ChangeHistoryDataSource
) : ChangeHistoryRepository {

    override fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory {
        return dataSource.addChangeHistory(changeHistory)
    }

    override fun getHistoryByProjectID(projectId: Int): List<ChangeHistory> {
        return dataSource.getByProjectId(projectId)
    }
    override fun getHistoryByTaskID(taskId: Int): List<ChangeHistory> {
        return dataSource.getByTaskId(taskId)
    }
}
