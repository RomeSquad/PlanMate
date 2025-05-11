package org.example.data.datasource.changelog

import org.example.logic.entity.ChangeHistory

interface ChangeHistoryDataSource {
    suspend fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory
    suspend fun getByProjectId(projectId: Int): List<ChangeHistory>
    suspend fun getByTaskId(taskId: Int): List<ChangeHistory>
}