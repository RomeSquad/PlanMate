package org.example.data.datasource.project

import org.example.data.datasource.mapper.fromCsvRowToProject
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.Project
import java.io.File

class CsvProjectDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val projectsFile: File
) : ProjectDataSource {

    override suspend fun getAllProjects(): List<Project> {
        val data = csvFileReader.readCsv(projectsFile)
        val projects = data.map { it.fromCsvRowToProject() }
        return projects
    }

    override suspend fun saveAllProjects(projects: List<Project>) {
        projects.forEach { project ->
            val csvRow = project.toCsvRow()
            csvFileWriter.writeCsv(projectsFile, listOf(csvRow))
        }

    }
}