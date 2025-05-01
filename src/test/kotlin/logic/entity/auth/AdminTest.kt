package logic.entity.auth

import org.example.logic.entity.auth.Admin
import org.example.logic.entity.auth.UserRole
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AdminTest() {

    @Test
    fun `should return Admin in formatted string`() {

        //given
        val admin = Admin(
            userId = 123,
            username = "user2",
            password = "ADF3",
            userRole = UserRole.ADMIN
        )
        val expected = formatter(
            userId = 123,
            username = "user2",
            password = "ADF3",
            userRole = UserRole.ADMIN
        )

        //when
        val displayed = admin.toString()

        //then
        assertEquals(displayed, expected)

    }
  
    fun formatter(
        userId: Int,
        username: String,
        password: String,
        userRole: UserRole
    ): String {
        return String.format(
            "%-10s | %-15s | %-10s | %-10s",
            "ID: $userId",
            "Username: $username",
            "Pass: $password",
            "Role: $userRole"
        )
    }
}