package org.example.data.datasource.project

import org.example.data.datasource.mapper.fromCsvRowToProject
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.Project
import org.example.logic.entity.User
import java.io.File
import java.util.*

class CsvProjectDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val projectsFile: File
) : ProjectDataSource {
    override suspend fun createProject(
        project: Project,
        user: User
    ): UUID {
        val row = project.toCsvRow()
        csvFileWriter.writeCsv(projectsFile, listOf(row))
        return project.projectId
    }

    override suspend fun getAllProjects(): List<Project> {
        val data = csvFileReader.readCsv(projectsFile)
        val projects = data.map { it.fromCsvRowToProject() }
        return projects
    }

    override suspend fun getProjectById(id: UUID): Project {
        val projects = getAllProjects()
        return projects.firstOrNull { it.projectId == id }
            ?: throw Exception("Project with id $id not found")
    }

    override suspend fun saveAllProjects() {
        val projects = getAllProjects()
        val rows = projects.map { it.toCsvRow() }
        csvFileWriter.writeCsv(projectsFile, rows)
    }

    override suspend fun editProject(project: Project) {
        val projects = getAllProjects().toMutableList()
        val index = projects.indexOfFirst { it.projectId == project.projectId }

        if (index == -1) {
            throw Exception("Project with id ${project.projectId} not found")
        }

        projects[index] = project
        saveAllProjects()
    }

    override suspend fun deleteProject(id: UUID) {
        val projects = getAllProjects().toMutableList()
        val removed = projects.removeIf { it.projectId == id }

        if (!removed) {
            throw (NoSuchElementException("Project with id $id not found"))
        }

        saveAllProjects()
    }


}