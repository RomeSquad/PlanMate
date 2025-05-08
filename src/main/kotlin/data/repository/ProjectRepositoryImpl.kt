package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import org.example.logic.entity.toProject
import org.example.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    private val projects by lazy { getAllProjects().getOrDefault(emptyList()).toMutableList() }


    override fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse> {
        return projectRequest.toProject(getLatestProjectId()).run {
            projects.add(this)
            Result.success(CreateProjectResponse(id))
        }
    }

    override fun editProject(project: Project): Result<Unit> {
        val index = projects.indexOfFirst { it.id == project.id }
        if (index != -1) {
            projects[index] = project
        } else {
            throw Exception("Project with id ${project.id} not found")
        }
        return TODO("Provide the return value")
    }

    override fun getProjectById(id: Int): Result<Project> {
        return projects.firstOrNull { it.id == id }?.let { Result.success(it) } ?: Result.failure(
            Exception("Project not found")
        )
    }

    override fun getAllProjects(): Result<List<Project>> {
        return projectDataSource.getAllProjects()
    }

    override fun saveAllProjects(): Result<Unit> {
        return projectDataSource.saveAllProjects(projects)
    }

    override fun deleteProject(id: Int): Result<Unit> {
        return projects.removeIf { it.id == id }.let {
            if (it) {
                Result.success(Unit)
            } else {
                Result.failure(NoSuchElementException("Project with id $id not found"))
            }
        }
    }

    private fun getLatestProjectId() = projects.lastOrNull()?.id ?: 0

}