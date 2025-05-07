package org.example.logic.usecase

import org.example.logic.entity.Project
import org.example.logic.repository.ChangeHistoryRepository

class ShowProjectHistoryUseCase(
    private val repository: ChangeHistoryRepository
) {
    fun execute(projectId: Int): Result<Project> {
        return repository.ShowHistoryByProjectID(projectId)
    }
}

