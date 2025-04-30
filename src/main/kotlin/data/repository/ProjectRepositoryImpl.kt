package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {

   fun insertProject(project: Project) {

    }


    fun editProject(project: Project) {
        if (project.id.isBlank()) return

        // Check if project exists
        val existing = projectDataSource.getProjectById(project.id) ?: return

        // If the data didn't change, don't update
        if (existing.name == project.name && existing.description == project.description) return

        // Update if different
        projectDataSource.editProject(project)
    }


     fun deleteProject(id: String) {

    }

     fun getProjectById(id: String): Project? {
        return null
    }
}