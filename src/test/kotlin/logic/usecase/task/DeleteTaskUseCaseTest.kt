package logic.usecase.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `should delete task successfully`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

        coEvery { taskRepository.deleteTask(projectId, taskId) } just Runs

        deleteTaskUseCase.deleteTask(projectId, taskId)

        coVerify(exactly = 1) { taskRepository.deleteTask(projectId, taskId) }
    }

//    @Test
//    fun `should throw exception when projectId is blank`() = runTest {
//        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//        val exception = assertThrows<IllegalArgumentException> {
//            deleteTaskUseCase.deleteTask(projectId, taskId)
//        }
//
//        assertEquals("projectId must not be blank", exception.message)
//        coVerify { taskRepository wasNot Called }
//    }

//    @Test
//    fun `should throw exception when taskId is blank`() = runTest {
//        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//        val exception = assertThrows<IllegalArgumentException> {
//            deleteTaskUseCase.deleteTask(projectId, taskId)
//        }
//
//        assertEquals("taskId must not be blank", exception.message)
//        coVerify { taskRepository wasNot Called }
//    }

//    @Test
//    fun `should propagate exception from repository`() = runTest {
//        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//
//        coEvery { taskRepository.deleteTask(projectId, taskId) } throws RuntimeException("Task not found")
//
//        val exception = assertThrows<RuntimeException> {
//            deleteTaskUseCase.deleteTask(projectId, taskId)
//        }
//
//        assertEquals("Task not found", exception.message)
//        coVerify { taskRepository.deleteTask(projectId, taskId) }
//    }
}
