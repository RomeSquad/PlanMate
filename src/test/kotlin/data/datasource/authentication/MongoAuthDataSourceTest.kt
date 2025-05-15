package data.datasource.authentication

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.example.logic.request.CreateUserRequest
import org.example.data.datasource.authentication.MongoAuthDataSource
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.LoginRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class MongoAuthDataSourceTest {
    private lateinit var authDataSource: MongoAuthDataSource
    private lateinit var userMongoCollection: MongoCollection<User>

    @BeforeEach
    fun setup() {
        userMongoCollection = mockk(relaxed = true)
        authDataSource = MongoAuthDataSource(userMongoCollection)
    }

    @Test
    fun `should insert user with valid data when insertUser is called `() = runTest {
        // Given
        val username = "testUser"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )

        // When
        val result = authDataSource.insertUser(request)

        // Then
        Assertions.assertEquals(username, result.username)

    }

    @Test
    fun `should return empty list when getAllUsers is called and no users exist`() = runTest {
        // Given
        val expectedUsers = emptyList<User>()
        coEvery { userMongoCollection.find().toList() } returns expectedUsers

        // When
        val result = authDataSource.getAllUsers()

        // Then
        Assertions.assertEquals(expectedUsers, result)
    }


    @Test
    fun `should throw UserNotFoundException when loginUser is called with invalid credentials`() = runTest {
        // Given
        val username = "testUser"
        val password = "password123"
        val hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99"
        val request = LoginRequest(username, password)
        val filter = Filters.and(
            Filters.eq("username", username),
            Filters.eq("password", hashedPassword)
        )
        coEvery { userMongoCollection.find(filter).firstOrNull() } returns null

        // When // Then
        assertThrows<UserNotFoundException> {
            authDataSource.loginUser(request)
        }
    }

    @Test
    fun `should throw UserNotFoundException when deleteUser is called with non-existing username`() = runTest {
        // Given
        val username = "testUser"
        val deleteResult = mockk<DeleteResult> {
            every { wasAcknowledged() } returns false // Simulate no acknowledgment
        }
        coEvery { userMongoCollection.deleteOne(Filters.eq("username", username)) } returns deleteResult

        // When / Then
        assertThrows<UserNotFoundException> {
            authDataSource.deleteUser(username)
        }
    }

    @Test
    fun `should throw UserNotFoundException when editUser is called with non-existing username`() = runTest {
        // Given
        val username = "testUser"
        val user = User(
            userId = UUID.randomUUID(),
            username = username,
            password = "newHashedPassword",
            userRole = UserRole.ADMIN
        )
        val filter = Filters.eq("username", username)
        coEvery { userMongoCollection.find(filter).firstOrNull() } returns null

        // When // Then
        assertThrows<UserNotFoundException> {
            authDataSource.editUser(user)
        }
    }

    @Test
    fun `should throw UserNotFoundException when editUser is called and update is acknowledged`() = runTest {
        // Given
        val username = "testUser"
        val user = User(
            userId = UUID.randomUUID(),
            username = username,
            password = "newHashedPassword",
            userRole = UserRole.ADMIN
        )
        val filter = Filters.eq("username", username)
        coEvery { userMongoCollection.find(filter).firstOrNull() } returns user
        val updateResult = mockk<UpdateResult> {
            every { wasAcknowledged() } returns true // Simulate successful update
        }
        coEvery {
            userMongoCollection.updateOne(
                filter,
                Updates.combine(
                    Updates.set("password", user.password),
                    Updates.set("role", user.userRole)
                )
            )
        } returns updateResult

        // When / Then
        assertThrows<UserNotFoundException> {
            authDataSource.editUser(user)
        }
    }

    @Test
    fun `should return null when getUserByUserName is called with non-existing username`() = runTest {
        // Given
        val username = "testUser"
        coEvery { userMongoCollection.find(Filters.eq("username", username)).firstOrNull() } returns null

        // When
        val result = authDataSource.getUserByUserName(username)

        // Then
        Assertions.assertNull(result)
    }
}







