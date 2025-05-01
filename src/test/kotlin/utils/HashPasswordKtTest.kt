package utils

import org.example.utils.hashPassword
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HashPasswordKtTest {

    @Test
    fun `hashPassword fun should return correct MD5 hash`() {
        // Given
        val password = "password123"
        val expected = "482c811da5d5b4bc6d497ffa98491e38"

        // When
        val hashedPassword = hashPassword(password)

        // Then
        assertEquals(expected, hashedPassword)
    }
}