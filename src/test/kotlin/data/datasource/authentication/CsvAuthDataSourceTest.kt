package data.datasource.authentication

import io.mockk.*
import org.example.data.datasource.authentication.CsvAuthDataSource
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File


class CsvAuthDataSourceTest {

    private lateinit var authDataSource: CsvAuthDataSource
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var csvFileWriter: CsvFileWriter
    private val usersFile = File("users.csv")


    @BeforeEach
    fun setup() {
        csvFileReader = mockk()
        csvFileWriter = mockk()
        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter)
    }

    @Test
    fun `should return list of users from CSV data when getAllUsers fun call `() {

        //Given
        val csvRows = listOf(readTestUser1List, readTestUser2List)
        val expectedUsers = listOf(testUser1, testUser2)

        every { csvFileReader.readCsv(usersFile) } returns csvRows

        //When
        val result = authDataSource.getAllUsers()

        //Then
        Assertions.assertEquals(expectedUsers.map { it.userId }, result.getOrNull()?.map { it.userId })
        verify { csvFileReader.readCsv(usersFile) }

    }

    @Test
    fun `should write users to CSV file when saveAllUsers fun call`() {

        //Given
        val csvRows = listOf(readTestUser1List, readTestUser2List)
        val expectedUsers = listOf(testUser1, testUser2)

        every { csvFileWriter.writeCsv(usersFile, any()) } just Runs

        //When
        val result = authDataSource.saveAllUsers(expectedUsers)

        //Then
        Assertions.assertTrue(result.isSuccess)

    }

}

val readTestUser1List = listOf("1", "amr", "482c811da5d5b4bc6d497ffa98491e38", "ADMIN")
val readTestUser2List = listOf("2", "nasser", "482c811da5d5b4bc6d497ffa98491e38", "MATE")
val testUser1 = object : User {
    override val userId: Int = 1
    override val username: String = "amr"
    override val password: String = "482c811da5d5b4bc6d497ffa98491e38"
    override val userRole: UserRole = UserRole.ADMIN
}
val testUser2 = object : User {
    override val userId: Int = 2
    override val username: String = "nasser"
    override val password: String = "482c811da5d5b4bc6d497ffa98491e38"
    override val userRole: UserRole = UserRole.MATE
}
