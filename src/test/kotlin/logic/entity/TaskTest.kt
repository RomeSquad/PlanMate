package logic.entity

import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
class TaskTest{
    @Test
    fun `should return Task in formatted String`(){
        //given
        val task=Task(
            id = "A12",
            title = "Implement Login",
            description = "Create login screen with validation",
            state =State(
                projectId = "AR12",
                name ="In Progress"
            ),
            projectId = "AR12",
            createdBy = "user10",
            createdAt = 13245,
            updatedAt = 123456
        )
          val expected =formatTask(
              id = "A12",
              title = "Implement Login",
              description = "Create login screen with validation",
              state =State(
                  projectId = "AR12",
                  name ="In Progress"
              ),
              projectId = "AR12",
              createdBy = "user10",
              createdAt = 13245,
              updatedAt = 123456
          )

//        )
        //when
        val displayed = task.toString()
        //then
        assertEquals(displayed,expected)
    }
    // helper
    fun formatTask(id : String,
                   title : String,
                   description : String,
                   state: State =State( projectId = "AR12", name ="In Progress"),
                   projectId : String,
                   createdBy : String,
                   createdAt : Int,
                   updatedAt : Int): String {
        return String.format(
            "%-10s | %-20s | %-20s | %-30s | %-15s | %-15s | %-10s | %-10s",
            "ID: $id",
            "Title: $title",
            "Description: $description",
            "State: $state",
            "ProjectID: $projectId",
            "Created by: $createdBy",
            "Created: $createdAt",
            "Updated: $updatedAt"
        )
    }
}