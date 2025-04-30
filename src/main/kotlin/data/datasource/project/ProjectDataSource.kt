package org.example.data.datasource.project
import  org.example.logic.entity.Project


import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project

interface ProjectDataSource {
    fun editProject(project: Project)
    fun getProjectById(id: String): Project?
    fun insertProject(projectRequest: CreateProjectRequest):Result<CreateProjectResponse>
    fun getAllProjects(): Result<List<Project>>
}