package org.example.data.repository

import org.example.data.datasource.changelog.ChangeHistoryDataSource
import org.example.logic.entity.ModificationLog
import org.example.logic.repository.ChangeHistoryRepository
import java.util.*

class ChangeHistoryRepositoryImpl(
    private val dataSource: ChangeHistoryDataSource
) : ChangeHistoryRepository {

    override suspend fun addChangeHistory(changeHistory: ModificationLog): ModificationLog{
        return dataSource.addChangeHistory(changeHistory)
    }

    override suspend fun getHistoryByProjectID(projectId: UUID): List<ModificationLog> {
        return dataSource.getByProjectId(projectId)
    }

    override suspend fun getHistoryByTaskID(taskId: UUID): List<ModificationLog> {
        return dataSource.getByTaskId(taskId)
    }
}
