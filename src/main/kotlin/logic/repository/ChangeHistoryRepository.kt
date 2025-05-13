package org.example.logic.repository

import org.example.logic.entity.ChangeHistory
import java.util.UUID

interface ChangeHistoryRepository {
    suspend fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory
    suspend fun getHistoryByProjectID(projectId: UUID): List<ChangeHistory>
    suspend fun getHistoryByTaskID(taskId: UUID): List<ChangeHistory>
}