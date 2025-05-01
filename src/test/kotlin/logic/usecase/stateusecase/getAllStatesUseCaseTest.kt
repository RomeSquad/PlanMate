package logic.usecase.stateusecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.stateusecase.GetAllStatesUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class getAllStatesUseCaseTest{
  lateinit var stateRepository: StateRepository
  lateinit var getAllStatesUseCase: GetAllStatesUseCase

  @BeforeEach
  fun setUp() {
   stateRepository = mockk()
   getAllStatesUseCase = GetAllStatesUseCase(stateRepository)
  }

 @Test
 fun `should return all the state in the same project when call all states`(){
  val allStates= listOf(State(name = "todo", projectId = "1"), State(name = "done", projectId = "1"))

  every { stateRepository.getAllStates() } returns allStates
  val result = stateRepository.getAllStates()

  Assertions.assertEquals(allStates, result)
  verify { stateRepository.getAllStates() }
 }


 }