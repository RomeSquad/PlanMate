package data.datasource.authentication

import org.example.data.datasource.authentication.CsvAuthenticationDataSourceImpl
import org.example.logic.entity.auth.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CsvAuthenticationDataSourceImplTest {

    private lateinit var dataSource: CsvAuthenticationDataSourceImpl

    @BeforeEach
    fun setup() {
        dataSource = CsvAuthenticationDataSourceImpl()
    }

    // region CsvAuthenticationDataSourceImpl Success

    @Test
    fun `should insert and return user when registering valid user`() {
        // Given
        val username = "amr"
        val password = "hashedPassword"
        val userRole = UserRole.ADMIN

        // When
        val result = dataSource.insertUser(username, password, userRole)

        // Then
        assertTrue(result.isSuccess)

    }

    @Test
    fun `should return user when getting user by valid id`() {
        // Given
        val username = "amr"
        val password = "hashedPassword"
        val userRole = UserRole.ADMIN
        val insertResult = dataSource.insertUser(username, password, userRole)
        val insertedUser = insertResult.getOrThrow()

        // When
        val result = dataSource.getUserById(insertedUser.userId)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return user when getting user by valid username`() {
        // Given
        val username = "amr"
        val password = "hashedPassword"
        val userRole = UserRole.ADMIN
        dataSource.insertUser(username, password, userRole)

        // When
        val result = dataSource.getUserByUsername(username)

        // Then
        assertTrue(result.isSuccess)
    }

    // endregion

    // region CsvAuthenticationDataSourceImpl Failure

    // region Failure insertUser

    @Test
    fun `should throw exception when inserting user with empty username`() {
        // Given
        val username = ""
        val password = "hashedPassword"
        val userRole = UserRole.ADMIN

        // When / Then
        assertFailsWith<IllegalArgumentException> {
            dataSource.insertUser(username, password, userRole)
        }
    }

    // endregion

    // region Failure getUserById
    @Test
    fun `should throw exception when getting user by negative id`() {
        // Given
        val userId = -1

        // When // Then
        assertFailsWith<IllegalArgumentException> {
            dataSource.getUserById(userId)
        }
    }

    // endregion

    // region Failure getUserByUsername


    @Test
    fun `should throw exception when getting user by empty username`() {
        // Given
        val username = ""

        // When / Then
        assertFailsWith<IllegalArgumentException> {
            dataSource.getUserByUsername(username)
        }
    }

    // endregion

    // endregion
}