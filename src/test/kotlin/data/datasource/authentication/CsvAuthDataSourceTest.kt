package data.datasource.authentication

import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.request.auth.CreateUserRequest
import org.example.data.datasource.authentication.CsvAuthDataSource
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.data.utils.hashStringWithMD5
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.auth.LoginRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.util.*

class CsvAuthDataSourceTest {

    private lateinit var authDataSource: CsvAuthDataSource
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var csvFileWriter: CsvFileWriter
    private val usersFile = File("users.csv")

    @BeforeEach
    fun setup() {
        csvFileReader = mockk(relaxed = true)
        csvFileWriter = mockk(relaxed = true)

        every { csvFileReader.readCsv(usersFile) } returns emptyList()

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)
    }

    @Test
    fun `should return list of users from CSV data when getAllUsers is called`() = runTest {
        // Given
        val csvRows = listOf(readTestUser1List, readTestUser2List)
        every { csvFileReader.readCsv(usersFile) } returns csvRows

        // When
        val result = authDataSource.getAllUsers()

        // Then
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(testUser1.userId, result[0].userId)
        Assertions.assertEquals(testUser2.userId, result[1].userId)
    }

    @Test
    fun `should insert user with valid data`() = runTest {
        // Given
        val username = "testUser"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )

        every { csvFileReader.readCsv(usersFile) } returns emptyList()
        every { csvFileWriter.writeCsv(usersFile, any()) } just runs

        // When
        val result = authDataSource.insertUser(request)

        // Then
        Assertions.assertEquals(username, result.username)
        Assertions.assertEquals(userRole, result.userRole)
        verify { csvFileWriter.writeCsv(usersFile, any()) }
    }


    @Test
    fun `should throw exception when login with incorrect password`() = runTest {
        // Given
        val username = "testUser"
        val correctPasswordHash = "5f4dcc3b5aa765d61d8327deb882cf99"
        val wrongPassword = "wrongPassword"
        val wrongPasswordHash = "wrongHash"

        val user = User(
            userId = UUID.randomUUID(),
            username = username,
            password = correctPasswordHash,
            userRole = UserRole.ADMIN
        )

        mockkStatic(::hashStringWithMD5)
        every { hashStringWithMD5(wrongPassword) } returns wrongPasswordHash

        val existingUsers = listOf(
            listOf(user.userId.toString(), username, correctPasswordHash, UserRole.ADMIN.name)
        )
        every { csvFileReader.readCsv(usersFile) } returns existingUsers

        val loginRequest = LoginRequest(
            username = username,
            password = wrongPassword
        )

        // When/Then
        assertThrows<UserNotFoundException> {
            authDataSource.loginUser(loginRequest)
        }

        unmockkStatic(::hashStringWithMD5)
    }

    @Test
    fun `should throw exception when login with non-existing username`() = runTest {
        // Given
        val nonExistingUsername = "nonExistingUser"
        val password = "password123"

        mockkStatic(::hashStringWithMD5)
        every { hashStringWithMD5(password) } returns "hashedPassword"

        every { csvFileReader.readCsv(usersFile) } returns emptyList()

        val loginRequest = LoginRequest(
            username = nonExistingUsername,
            password = password
        )

        // When/Then
        assertThrows<UserNotFoundException> {
            authDataSource.loginUser(loginRequest)
        }

        unmockkStatic(::hashStringWithMD5)
    }


    @Test
    fun `should throw exception when deleting non-existing user`() = runTest {
        // Given
        val nonExistingUsername = "nonExistingUser"

        every { csvFileReader.readCsv(usersFile) } returns emptyList()

        // When/Then
        assertThrows<UserNotFoundException> {
            authDataSource.deleteUser(nonExistingUsername)
        }
    }


    @Test
    fun `should throw exception when editing non-existing user`() = runTest {
        // Given
        val nonExistingUsername = "nonExistingUser"

        every { csvFileReader.readCsv(usersFile) } returns emptyList()

        val user = User(
            userId = UUID.randomUUID(),
            username = nonExistingUsername,
            password = "hashedPassword",
            userRole = UserRole.ADMIN
        )

        // When/Then
        assertThrows<UserNotFoundException> {
            authDataSource.editUser(user)
        }
    }

    @Test
    fun `should delete user when username exists`() = runTest {
        // Given
        val username = "userToDelete"
        val userId = UUID.randomUUID()

        val userRow = listOf(userId.toString(), username, "hashedPassword", UserRole.ADMIN.name)

        every { csvFileReader.readCsv(usersFile) } returns listOf(userRow)
        every { csvFileWriter.writeCsv(usersFile, any()) } just runs

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)

        // When
        val result = authDataSource.deleteUser(username)

        // Then
        Assertions.assertTrue(result)
    }

    @Test
    fun `should throw exception when inserting user with existing username`() = runTest {
        // Given
        val username = "existingUser"
        val existingUserRow =
            listOf(UUID.randomUUID().toString(), username, "hashedPassword", UserRole.ADMIN.name)

        every { csvFileReader.readCsv(usersFile) } returns listOf(existingUserRow)

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)

        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.MATE
        )

        // When/Then
        assertThrows<UserNameAlreadyExistsException> {
            authDataSource.insertUser(request)
        }
    }

    @Test
    fun `should login user with valid credentials`() = runTest {
        // Given
        val username = "testUser"
        val password = "password123"
        val hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99"

        val userRow =
            listOf(UUID.randomUUID().toString(), username, hashedPassword, UserRole.ADMIN.name)

        mockkStatic(::hashStringWithMD5)
        every { hashStringWithMD5(password) } returns hashedPassword

        every { csvFileReader.readCsv(usersFile) } returns listOf(userRow)

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)

        val loginRequest = LoginRequest(
            username = username,
            password = password
        )

        // When
        val result = authDataSource.loginUser(loginRequest)

        // Then
        Assertions.assertEquals(username, result.username)
        Assertions.assertEquals(UserRole.ADMIN, result.userRole)

        unmockkStatic(::hashStringWithMD5)
    }

    @Test
    fun `should edit user when username exists`() = runTest {
        // Given
        val username = "userToEdit"
        val userId = UUID.randomUUID()
        val originalRole = UserRole.MATE
        val newRole = UserRole.ADMIN

        val userRow = listOf(userId.toString(), username, "hashedPassword", originalRole.name)

        every { csvFileReader.readCsv(usersFile) } returns listOf(userRow)
        every { csvFileWriter.writeCsv(usersFile, any()) } just runs

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)

        val updatedUser = User(
            userId = userId,
            username = username,
            password = "hashedPassword",
            userRole = newRole
        )

        // When
        authDataSource.editUser(updatedUser)

        // Then
        verify { csvFileWriter.writeCsv(usersFile, any()) }
    }

    @Test
    fun `should return user when getUserByUsername with existing username`() = runTest {
        // Given
        val username = "existingUser"
        val userId = UUID.randomUUID()

        val userRow = listOf(userId.toString(), username, "hashedPassword", UserRole.ADMIN.name)

        every { csvFileReader.readCsv(usersFile) } returns listOf(userRow)

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)

        // When
        val result = authDataSource.getUserByUserName(username)

        // Then
        Assertions.assertNotNull(result)
        Assertions.assertEquals(username, result?.username)
    }

    @Test
    fun `should return null when getUserByUsername with non-existing username`() = runTest {
        // Given
        val nonExistingUsername = "nonExistingUser"

        every { csvFileReader.readCsv(usersFile) } returns emptyList()

        // When
        val result = authDataSource.getUserByUserName(nonExistingUsername)

        // Then
        Assertions.assertNull(result)
    }

    @Test
    fun `should save all users`() = runTest {
        // Given
        val user1 = User(
            userId = UUID.randomUUID(),
            username = "user1",
            password = "hashedPassword1",
            userRole = UserRole.ADMIN
        )
        val user2 = User(
            userId = UUID.randomUUID(),
            username = "user2",
            password = "hashedPassword2",
            userRole = UserRole.MATE
        )

        val initialUsers = listOf(
            listOf(user1.userId.toString(), user1.username, user1.password, user1.userRole.name),
            listOf(user2.userId.toString(), user2.username, user2.password, user2.userRole.name)
        )
        every { csvFileReader.readCsv(usersFile) } returns initialUsers
        every { csvFileWriter.writeCsv(usersFile, any()) } just runs

        authDataSource = CsvAuthDataSource(csvFileReader, csvFileWriter, usersFile)

        // When
        authDataSource.saveAllUsers()

        // Then
        verify(exactly = 2) { csvFileWriter.writeCsv(usersFile, any()) }
    }
}

// Test data
val readTestUser1List = listOf(
    "ef27b62a-3983-4ef9-8b2c-9fa75bf5a91d",
    "amr",
    "482c811da5d5b4bc6d497ffa98491e38",
    "ADMIN"
)
val readTestUser2List = listOf(
    "cef27b62a-3983-4ef9-8b2c-9fa75bf5a91",
    "nasser",
    "482c811da5d5b4bc6d497ffa98491e38",
    "MATE"
)

val testUser1 = User(
    userId = UUID.fromString(readTestUser1List[0]),
    username = readTestUser1List[1],
    password = readTestUser1List[2],
    userRole = UserRole.valueOf(readTestUser1List[3])
)

val testUser2 = User(
    userId = UUID.fromString(readTestUser2List[0]),
    username = readTestUser2List[1],
    password = readTestUser2List[2],
    userRole = UserRole.valueOf(readTestUser2List[3])
)