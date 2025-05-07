package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.Project
import org.example.logic.repository.ChangeHistoryRepository

class ChangeHistoryRepositoryImpl(private val dataSource: ProjectDataSource
) : ChangeHistoryRepository {

    override fun ShowHistoryByProjectID(projectId: Int): Result<Project> {

            return dataSource.getAllProjects().fold(
                onSuccess = { list ->
                    list.firstOrNull { it.id == projectId }
                        ?.let { Result.success(it) }
                        ?: Result.failure(NoSuchElementException("Project $projectId not found"))
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        }

    }

