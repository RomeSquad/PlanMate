package org.example.data.datasource.project
import  org.example.logic.entity.Project


interface ProjectDataSource {
    fun getAllProjects(): Result<List<Project>>
    fun saveAllProjects(projects: List<Project>):Result<Unit>
}