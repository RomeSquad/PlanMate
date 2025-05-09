package org.example.logic.usecase.history

import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository

class ShowProjectHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    suspend fun execute(projectId: Int): List<ChangeHistory> {
        return repository.getHistoryByProjectID(projectId)
    }

}