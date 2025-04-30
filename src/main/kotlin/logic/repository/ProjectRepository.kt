package org.example.logic.repository

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project

interface ProjectRepository {
    fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse>
    fun getAllProjects(): Result<List<Project>>
    fun editProject(project: Project)
}