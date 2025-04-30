package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import org.example.logic.entity.toProject
import org.example.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

    private var projects = mutableListOf<Project>()

    init {
        projects += getAllProjects().getOrThrow()
    }

    override fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse> {
        return projectRequest.toProject(getLatestProjectId()).run {
            projects.add(this)
            Result.success(CreateProjectResponse(id))
        }
    }

    override fun getAllProjects(): Result<List<Project>> {
        return projectDataSource.getAllProjects()
    }
    private fun getLatestProjectId() = projects.lastOrNull()?.id ?: 0

    override fun editProject(project: Project) {


        var existingProject = projectDataSource.getProjectById(project.id) ?: return

        val oldChangeHistory = existingProject.changeHistory.toMutableList()
        val newHistory = ChangeHistory(
        /*TODO*/
        )

        val newProject = existingProject.copy(
            changeHistory = oldChangeHistory+newHistory,
            name = project.name,
            description = project.description
        )
        projects = projects.map {
            if(it.id == project.id) {
                newProject
            }else{
                it
            }
        }.toMutableList()
        if (existingProject.name == project.name &&
            existingProject.description == project.description
        ) return

        projectDataSource.editProject(project)
    }
}

// project ->name , id , desc
//.copy (name = newName)
// project -> newName , id , desc