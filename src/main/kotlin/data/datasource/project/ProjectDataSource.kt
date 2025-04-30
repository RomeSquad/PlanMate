package org.example.data.datasource.project
import  org.example.logic.entity.Project


import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse

interface ProjectDataSource {
    fun getAllProjects(): Result<List<Project>>
    fun saveAllProjects(projects: List<Project>):Result<Unit>
}