package org.example.logic.usecase.history

import org.example.logic.entity.ModificationLog
import org.example.logic.repository.ChangeHistoryRepository
import java.util.*

class AddChangeHistoryUseCase(private val repository: ChangeHistoryRepository) {

    suspend fun execute(
        projectId: UUID,
        taskId: UUID?,
        authorId: UUID,
        changeDate: Date,
        changeDescription: String
    ): ModificationLog {
        try {
            val changeHistory = ModificationLog(
                projectID = projectId,
                taskID = taskId,
                authorID = authorId,
                changeDate = changeDate,
                changeDescription = changeDescription
            )
            return repository.addChangeHistory(changeHistory)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid Change History data: ${e.message}")
        }
    }
}