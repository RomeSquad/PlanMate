package org.example.logic.repository

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import java.util.UUID

interface ProjectRepository {
    suspend fun insertProject(projectRequest: CreateProjectRequest): CreateProjectResponse
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectById(id: UUID): Project
    suspend fun saveAllProjects()
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
}