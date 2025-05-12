package org.example.logic.usecase.history

import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository
import java.util.UUID

class ShowTaskHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    suspend fun execute(taskId: UUID): List<ChangeHistory> {
        try {
            return repository.getHistoryByTaskID(taskId)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid Task ID: ${e.message}")
        }
    }
}