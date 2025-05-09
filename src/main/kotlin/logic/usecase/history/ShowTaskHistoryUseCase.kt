package org.example.logic.usecase.history

import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository

class ShowTaskHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    suspend fun execute(taskId: Int): List<ChangeHistory> {
        return repository.getHistoryByTaskID(taskId)
    }
}