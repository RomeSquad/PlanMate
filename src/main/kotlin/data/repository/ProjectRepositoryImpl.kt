package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository
import org.example.logic.request.auth.ProjectCreationRequest
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {
    override suspend fun createProject(request : ProjectCreationRequest): UUID {
        return projectDataSource.createProject(request)
    }

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }

    override suspend fun getProjectById(id: UUID): Project {
        return projectDataSource.getProjectById(id)
    }

    override suspend fun saveAllProjects() {
        projectDataSource.saveAllProjects()
    }

    override suspend fun editProject(project: Project) {
        projectDataSource.editProject(project)
    }

    override suspend fun deleteProject(id: UUID) {
        projectDataSource.deleteProject(id)
    }

}