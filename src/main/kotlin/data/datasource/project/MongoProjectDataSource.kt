package org.example.data.datasource.project

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.example.logic.entity.Project

class MongoProjectDataSource(
    val mongo: MongoCollection<Project>
) : ProjectDataSource {
    override suspend  fun getAllProjects(): List<Project> {
        return mongo.find().toList()
    }

    override suspend fun saveAllProjects(projects: List<Project>) {
        mongo.insertMany(projects)
    }
}