package org.example.logic.repository

import org.example.logic.entity.Project
import org.example.logic.entity.auth.User
import java.util.*

interface ProjectRepository {
    suspend fun createProject(project: Project, user: User): UUID
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectById(id: UUID): Project
    suspend fun saveAllProjects()
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
}