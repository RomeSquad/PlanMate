package org.example.data.datasource.changelog

import org.example.logic.entity.ModificationLog
import java.util.*

interface ChangeHistoryDataSource {
    suspend fun addChangeHistory(changeHistory: ModificationLog	): ModificationLog
    suspend fun getByProjectId(projectId: UUID): List<ModificationLog>
    suspend fun getByTaskId(taskId: UUID): List<ModificationLog	>
}