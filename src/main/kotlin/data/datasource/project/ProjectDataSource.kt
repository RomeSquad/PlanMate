package org.example.data.datasource.project

import org.example.logic.entity.Project
import org.example.logic.entity.User
import java.util.*


interface ProjectDataSource {

    suspend fun createProject(project: Project, user: User): UUID
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectById(id: UUID): Project
    suspend fun saveAllProjects()
    suspend fun editProject(project: Project)
    suspend fun deleteProject(id: UUID)
}