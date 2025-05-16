package data.datasource.authentication

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.datasource.authentication.dto.UserDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.authentication.MongoAuthDataSource
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.CreateUserRequest
import org.example.logic.request.EditUserRequest
import org.example.logic.request.LoginRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MongoAuthDataSourceTest {
    private lateinit var authDataSource: MongoAuthDataSource
    private lateinit var userMongoCollection: MongoCollection<UserDto>

    @BeforeEach
    fun setup() {
        userMongoCollection = mockk(relaxed = true)
        authDataSource = MongoAuthDataSource(userMongoCollection)
    }

    @Test
    fun `should insert user with valid data when insertUser is called `() = runTest {
        val username = "testUser"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )

        val result = authDataSource.insertUser(request)

        Assertions.assertEquals(username, result.username)

    }

    @Test
    fun `should return empty list when getAllUsers is called and no users exist`() = runTest {
        val expectedUsers = emptyList<UserDto>()
        coEvery { userMongoCollection.find().toList() } returns expectedUsers

        val result = authDataSource.getAllUsers()

        Assertions.assertEquals(expectedUsers, result)
    }


    @Test
    fun `should throw UserNotFoundException when loginUser is called with invalid credentials`() = runTest {
        val username = "testUser"
        val password = "password123"
        val hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99"
        val request = LoginRequest(username, password)
        val filter = Filters.and(
            Filters.eq("username", username),
            Filters.eq("password", hashedPassword)
        )
        coEvery { userMongoCollection.find(filter).firstOrNull() } returns null

        assertThrows<UserNotFoundException> {
            authDataSource.loginUser(request)
        }
    }

    @Test
    fun `should throw UserNotFoundException when deleteUser is called with non-existing username`() = runTest {
        val username = "testUser"
        val deleteResult = mockk<DeleteResult> {
            every { wasAcknowledged() } returns false
        }
        coEvery { userMongoCollection.deleteOne(Filters.eq("username", username)) } returns deleteResult

        assertThrows<UserNotFoundException> {
            authDataSource.deleteUser(username)
        }
    }

    @Test
    fun `should throw UserNotFoundException when editUser is called with non-existing username`() = runTest {
        val username = "testUser"
        val user = EditUserRequest(
            username = username,
            password = "newHashedPassword",
            userRole = UserRole.ADMIN
        )
        val filter = Filters.eq("username", username)
        coEvery { userMongoCollection.find(filter).firstOrNull() } returns null

        assertThrows<UserNotFoundException> {
            authDataSource.editUser(user)
        }
    }

    @Test
    fun `should throw UserNotFoundException when editUser is called and update is acknowledged`() = runTest {
        val username = "testUser"
        val user = UserDto(
            username = username,
            password = "newHashedPassword",
            userRole = UserRole.ADMIN
        )
        val request = EditUserRequest(
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

        assertThrows<UserNotFoundException> {
            authDataSource.editUser(request)
        }
    }

    @Test
    fun `should return null when getUserByUserName is called with non-existing username`() = runTest {
        val username = "testUser"
        coEvery { userMongoCollection.find(Filters.eq("username", username)).firstOrNull() } returns null

        val result = authDataSource.getUserByUserName(username)

        Assertions.assertNull(result)
    }
}







