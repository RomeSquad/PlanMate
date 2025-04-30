package org.example.data.datasource.project
import  org.example.logic.entity.Project


interface ProjectDataSource {
    fun insertProject(project: Project)
    fun editProject(project: Project)
    fun getProjectById(id: String): Project?
    fun deleteProject(id: String)
}