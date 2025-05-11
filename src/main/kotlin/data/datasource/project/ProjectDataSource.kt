package org.example.data.datasource.project

import  org.example.logic.entity.Project


interface ProjectDataSource {
    suspend fun getAllProjects(): List<Project>
    suspend fun saveAllProjects(projects: List<Project>)
}