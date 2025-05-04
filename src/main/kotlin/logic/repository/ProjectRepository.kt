package org.example.logic.repository

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project

interface ProjectRepository {
    fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse>
    fun getProjectById(id: Int): Result<Project>
    fun getAllProjects(): Result<List<Project>>
    fun saveAllProjects(): Result<Unit>
    fun  editProject(project: Project)
}