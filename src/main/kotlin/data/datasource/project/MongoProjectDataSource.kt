package org.example.data.datasource.project

import kotlinx.coroutines.flow.toList
import org.example.logic.entity.Project
import com.mongodb.kotlin.client.coroutine.MongoCollection

class MongoProjectDataSource (
    val mongo: MongoCollection<Project>
) : ProjectDataSource {
    override suspend  fun getAllProjects(): List<Project> {
        return mongo.find().toList()
    }

    override suspend fun saveAllProjects(projects: List<Project>){
            mongo.insertMany(projects)
    }
}