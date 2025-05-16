package org.example.logic.usecase.history

import org.example.logic.entity.ModificationLog
import org.example.logic.repository.ChangeHistoryRepository
import java.util.*

class ShowProjectHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    suspend fun execute(projectId: UUID): List<ModificationLog> {
        try {
            return repository.getHistoryByProjectID(projectId)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid Project ID: ${e.message}")
        }
    }

}