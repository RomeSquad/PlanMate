package org.example.data.repository

import kotlinx.coroutines.runBlocking
import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.Project
import org.example.logic.entity.auth.User
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

    override suspend fun createProject(
        project: Project,
        user: User
    ): UUID {
        val projectId = project.copy(
            UUID.randomUUID()
        )
        projects.add(projectId)
        projectDataSource.saveAllProjects(projects)
        return projectId.projectId
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

}