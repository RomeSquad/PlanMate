package org.example.data.datasource.projectState

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.datasource.projectState.ProjectStateDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.example.logic.entity.ProjectState
import java.util.UUID

class MongoProjectStateDataSource(
    private val mongo: MongoCollection<ProjectState>
) : ProjectStateDataSource {

    override suspend fun getAllProjectStates(): List<ProjectState> {
        return mongo.find().toList()
    }

    override suspend fun addProjectState(state: ProjectState) {
        mongo.insertOne(state)
    }

    override suspend fun editProjectState(projectId: UUID, newStateName: String) {
        val projectState = mongo.find(Filters.eq("_id", projectId)).firstOrNull()
        if (projectState != null) {
            val updatedProjectState = projectState.copy(stateName = newStateName)
            mongo.replaceOne(Filters.eq("_id", projectId), updatedProjectState)
        }
    }

    override suspend fun deleteProjectState(projectId: UUID) {
        mongo.deleteOne(Filters.eq("_id", projectId))
    }

    override suspend fun getStateById(projectId: UUID): ProjectState {
        return mongo.find(Filters.eq("_id", projectId)).firstOrNull()
            ?: throw IllegalArgumentException("ProjectState with id $projectId not found")
    }
}