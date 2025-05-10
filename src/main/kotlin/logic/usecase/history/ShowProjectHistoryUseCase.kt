package org.example.logic.usecase.history

import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository

class ShowProjectHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    suspend fun execute(projectId: Int): List<ChangeHistory> {
        try {
            return repository.getHistoryByProjectID(projectId)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid Project ID: ${e.message}")
        }
    }

}