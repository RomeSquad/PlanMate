package org.example.logic.usecase.history

import org.example.logic.entity.ModificationLog
import org.example.logic.repository.ChangeHistoryRepository
import java.util.*

class ShowTaskHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    suspend fun execute(taskId: UUID): List<ModificationLog> {
        try {
            return repository.getHistoryByTaskID(taskId)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid Task ID: ${e.message}")
        }
    }
}