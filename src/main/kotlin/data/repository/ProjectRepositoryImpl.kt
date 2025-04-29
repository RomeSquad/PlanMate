package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
) : ProjectRepository