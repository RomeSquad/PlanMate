package org.example.data.datasource.changelog

import org.example.logic.entity.ChangeHistory
import java.util.*

interface ChangeHistoryDataSource {
    suspend fun addChangeHistory(changeHistory: ChangeHistory): ChangeHistory
    suspend fun getByProjectId(projectId: UUID): List<ChangeHistory>
    suspend fun getByTaskId(taskId: UUID): List<ChangeHistory>
}