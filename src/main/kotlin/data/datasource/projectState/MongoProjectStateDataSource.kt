package org.example.data.datasource.projectState

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.example.data.datasource.state.ProjectStateDataSource
import org.example.logic.entity.ProjectState

class MongoProjectStateDataSource(
    val mongo: MongoCollection<ProjectState>
) : ProjectStateDataSource{

    override suspend fun getAllProjectStates(projectId: Int): List<ProjectState> {
        return mongo.find().toList().filter { it.projectId == projectId }
    }

    override suspend fun addProjectState(state: ProjectState) {
         mongo.insertOne(state)
    }

    override suspend fun editProjectState(
        projectId: Int,
        newStateName: String
    ) {
        val updateResult = Updates.combine(
            Updates.set("stateName",newStateName)
        )

        val editedState = mongo.updateOne(
            filter = Filters.eq("projectId",projectId),
            update = updateResult
        )

        if (editedState.matchedCount == 0L) {
            throw IllegalArgumentException("Project with ID $projectId not found")
        }
    }

    override suspend fun deleteProjectState(projectId: Int) {
        val filter = Filters.and(
            Filters.eq("projectId",projectId),
        )
        val deleteResult = mongo.deleteOne(filter)

        if(deleteResult.deletedCount == 0L){
            throw IllegalArgumentException("Project with Id $projectId is not found")
        }
    }

    override suspend fun getStateById(projectId: Int): ProjectState {
        return mongo.find(Filters.eq("projectId",projectId)).first()
    }
}
