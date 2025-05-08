package org.example.logic.usecase.history

import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository
import java.util.Date

class AddChangeHistoryUseCase(private val repository: ChangeHistoryRepository) {

    suspend fun execute(projectId: Int, taskId: Int, authorId: Int, changeDate: Date, changeDescription: String): ChangeHistory {
        try {
            val changeHistory = ChangeHistory(
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