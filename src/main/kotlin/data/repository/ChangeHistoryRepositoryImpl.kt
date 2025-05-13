package org.example.data.repository

import org.example.data.datasource.changelog.ChangeHistoryDataSource
import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository
import java.util.*

class ChangeHistoryRepositoryImpl(
    private val dataSource: ChangeHistoryDataSource
) : ChangeHistoryRepository {

    override suspend fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory {
        return dataSource.addChangeHistory(changeHistory)
    }

    override suspend fun getHistoryByProjectID(projectId: UUID): List<ChangeHistory> {
        return dataSource.getByProjectId(projectId)
    }

    override suspend fun getHistoryByTaskID(taskId: UUID): List<ChangeHistory> {
        return dataSource.getByTaskId(taskId)
    }
}
