package org.example.data.datasource.mapper

import data.datasource.authentication.dto.UserDto
import logic.request.auth.CreateUserRequest
import org.example.data.utils.hashStringWithMD5
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import java.util.*

fun UserDto.toCsvRow(): List<String> {
    return listOf(userId.toString(), username, password, userRole.toString())
}


fun List<String>.fromCsvRowToUser(): UserDto {
    return UserDto(
        userId = UUID.fromString(this[0]),
        username = this[1],
        password = this[2],
        userRole = UserRole.valueOf(this[3])
    )
}
fun UserDto.toUser() = User(
    userId = userId,
    username = username,
    userRole = userRole
)


fun CreateUserRequest.toUserDto() = UserDto(
    username = username,
    password = hashStringWithMD5(password),
    userRole = userRole
)