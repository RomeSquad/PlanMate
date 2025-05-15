package org.example.data.datasource.project

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.example.logic.entity.Project
import org.example.logic.entity.auth.User
import org.litote.kmongo.eq
import java.util.*

class MongoProjectDataSource(
    private val mongo: MongoCollection<Project>
) : ProjectDataSource {
    private var projects = mutableListOf<Project>()

    init {
        runBlocking {
            projects += getAllProjects()
        }
    }

    override suspend fun createProject(
        project: Project,
        user: User
    ): UUID {
        val projectId = UUID.randomUUID()
        val newProject = project.copy(projectId = projectId)
        projects.add(newProject)
        mongo.insertOne(newProject)
        return projectId
    }

    override suspend fun getProjectById(id: UUID): Project {
        return projects.firstOrNull { it.projectId == id }
            ?: throw Exception("Project with id $id not found")
    }

    override suspend fun saveAllProjects() {
        mongo.insertMany(getAllProjects())
    }

    override suspend fun editProject(project: Project) {

        val index = projects.indexOfFirst { it.projectId == project.projectId }
        if (index != -1) {
            projects[index] = project
            mongo.replaceOne(Project::projectId eq project.projectId, project)
        } else {
            throw Exception("Project with id ${project.projectId} not found")
        }
    }

    override suspend fun deleteProject(id: UUID) {
        val filter = Filters.and(
            Filters.eq(Project::projectId.name, id)
        )

        mongo.deleteOne(filter)
    }

    override suspend fun getAllProjects(): List<Project> {
        return mongo.find().toList()
    }

}