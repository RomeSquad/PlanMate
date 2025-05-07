package org.example.logic.repository

import org.example.logic.entity.ChangeHistory

interface ChangeHistoryRepository {
    suspend fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory
    suspend fun getHistoryByProjectID(projectId: Int): List<ChangeHistory>
    suspend fun getHistoryByTaskID(taskId: Int): List<ChangeHistory>
}