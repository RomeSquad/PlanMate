package org.example.data.repository

import kotlinx.coroutines.runBlocking
import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import org.example.logic.entity.toProject
import org.example.logic.repository.ProjectRepository
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    private var projects = mutableListOf<Project>()

    init {
        runBlocking {
            projects += getAllProjects()
        }
    }

    override suspend fun insertProject(projectRequest: CreateProjectRequest): CreateProjectResponse {
        val newProject = projectRequest.toProject()
        projects.add(newProject)
        projectDataSource.saveAllProjects(projects)
        return CreateProjectResponse(newProject.projectId)
    }

    override suspend fun editProject(project: Project) {
        val index = projects.indexOfFirst { it.projectId == project.projectId }
        if (index == -1) throw Exception("Project with id ${project.projectId} not found")
        projects[index] = project
        projectDataSource.saveAllProjects(projects)
    }

    override suspend fun getProjectById(id: UUID): Project {
        return projects.firstOrNull { it.projectId == id }
            ?: throw Exception("Project with id $id not found")
    }

    override suspend fun getAllProjects(): List<Project> {
        return projectDataSource.getAllProjects()
    }

    override suspend fun saveAllProjects() {
        projectDataSource.saveAllProjects(projects)
    }

    override suspend fun deleteProject(id: UUID) {
        return projects.removeIf { it.projectId == id }.let {
            if (!it) {
                throw (NoSuchElementException("Project with id $id not found"))
            }
        }
    }


    private fun getLatestProjectId() : UUID = UUID.randomUUID()

}