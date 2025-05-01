package org.example.data.datasource.project

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import org.example.logic.entity.State

import java.io.File
import kotlin.collections.listOf

    class CsvProjectDataSource(private val file: File) {
        private val projects = mutableListOf<Project>()

        init {
            if (file.exists()) {
                file.readLines()
                    .filter { it.isNotBlank() }
                    .forEach {
                        val parts = it.split(",")
                        if (parts.size >= 3) {
                            val project = Project(
                                0, parts[1], parts[2], listOf(), State()
                            )
                            projects.add(project)
                        }
                    }
            }
        }


         fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse> {

            if (projectRequest.name.isBlank()) {
                return Result.failure(IllegalArgumentException("Name cannot be blank"))
            }

              val nextId = (projects.maxOfOrNull { it.id } ?: 0) + 1

            val newProject = Project(
                id = nextId,
                name = projectRequest.name,
                description = projectRequest.description,
                changeHistory = listOf(),
                state = State()
            )

            projects.add(newProject)
            return Result.success(CreateProjectResponse(id = newProject.id))
        }
        fun getProjectById(id: Int): Project? {
            return projects.find { it.id == id }
        }
    }