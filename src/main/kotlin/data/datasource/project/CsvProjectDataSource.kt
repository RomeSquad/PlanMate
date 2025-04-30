package org.example.data.datasource.project

import data.utils.CustomFile
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CvsFileWriter
import org.example.data.utils.fromCsvRowToProject
import org.example.data.utils.toCsvRow
import org.example.logic.entity.Project

class CsvProjectDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CvsFileWriter,
) : ProjectDataSource {

    private val projectsFile = CustomFile("project3.csv")
    override fun getAllProjects(): Result<List<Project>> {
        val data = csvFileReader.readCsv(projectsFile)
        println(data[0])
        val projects = data.map { it.fromCsvRowToProject() }
        return Result.success(projects)
    }

    override fun saveAllProjects(projects: List<Project>): Result<Unit> {
        projects.forEach { project ->
            val csvRow = project.toCsvRow()
            csvFileWriter.writeCsv(projectsFile, listOf(csvRow))
        }
        return Result.success(Unit)
    }
}