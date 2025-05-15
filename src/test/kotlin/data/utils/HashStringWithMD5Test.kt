package data.utils

import org.example.data.utils.hashStringWithMD5
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HashStringWithMD5Test {
    @Test
    fun `hashStringWithMD5 should return correct MD5 hash`() {
        // Given
        val password = "password123"
        val expected = "482c811da5d5b4bc6d497ffa98491e38"

        // When
        val hashedPassword = hashStringWithMD5(password)

        // Then
        assertEquals(expected, hashedPassword)
    }
}