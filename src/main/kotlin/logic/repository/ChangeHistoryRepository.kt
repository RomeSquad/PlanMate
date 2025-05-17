package org.example.logic.repository

import org.example.logic.entity.ModificationLog
import java.util.*

interface ChangeHistoryRepository {
    suspend fun addChangeHistory(modificationLog: ModificationLog	): ModificationLog
    suspend fun getHistoryByProjectID(projectId: UUID): List<ModificationLog	>
    suspend fun getHistoryByTaskID(taskId: UUID): List<ModificationLog	>
}