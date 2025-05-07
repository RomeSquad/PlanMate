package org.example.logic.repository

import org.example.logic.entity.ChangeHistory

interface ChangeHistoryRepository {
        fun addChangeHistory(changeHistory: ChangeHistory):ChangeHistory
        fun getHistoryByProjectID(projectId: Int): List<ChangeHistory>
        fun getHistoryByTaskID(taskId: Int): List<ChangeHistory>
}