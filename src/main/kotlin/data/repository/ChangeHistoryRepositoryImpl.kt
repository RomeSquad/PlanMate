package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.Project
import org.example.logic.repository.ChangeHistoryRepository

class ChangeHistoryRepositoryImpl(private val dataSource: ProjectDataSource
) : ChangeHistoryRepository {

    override fun getHistoryByProjectId(projectId: Int): Result<List<Project>> {
        TODO("Not yet implemented")
    }

}
