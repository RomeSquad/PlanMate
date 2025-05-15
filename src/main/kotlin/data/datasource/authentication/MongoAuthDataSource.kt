package org.example.data.datasource.authentication

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.datasource.authentication.dto.UserDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.example.logic.request.CreateUserRequest
import org.example.data.datasource.mapper.toUser
import org.example.data.datasource.mapper.toUserDto
import org.example.data.utils.AuthConstants.PASSWORD
import org.example.data.utils.AuthConstants.USER_ID
import org.example.data.utils.AuthConstants.USER_NAME
import org.example.data.utils.AuthConstants.USER_ROLE
import org.example.data.utils.hashStringWithMD5
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.CreateUserRequest
import org.example.logic.request.EditUserRequest
import org.example.logic.request.LoginRequest
import java.util.UUID

class MongoAuthDataSource(
    private val userMongoCollection: MongoCollection<UserDto>
) : AuthDataSource {
    override suspend fun getAllUsers(): List<UserDto> {
        return userMongoCollection.find().toList()
    }

    override suspend fun insertUser(request: CreateUserRequest): UserDto {
        isUserNameExists(request.username)
        val newUser = request.toUserDto()
        userMongoCollection.insertOne(newUser)
        return newUser
    }

    override suspend fun loginUser(request: LoginRequest): UserDto {
        val hashedPassword = hashStringWithMD5(request.password)
        val filter = Filters.and(
            Filters.eq(User::username.name, request.username),
            Filters.eq(User::password.name, hashedPassword)
        )
        val user = userMongoCollection
            .find(filter)
            .firstOrNull()
            ?: throw UserNotFoundException()
        return user
    }

    override suspend fun deleteUser(username: String): Boolean {
        val filter = Filters.eq(User::username.name, username)
        val deleteResult = userMongoCollection
            .deleteOne(filter)
        return if (deleteResult.wasAcknowledged()) {
            true
        } else {
            throw UserNotFoundException()
        }
    }

    override suspend fun editUser(request : EditUserRequest) {
        val hashedPassword = hashStringWithMD5(request.password)

        val filter = Filters.eq(User::username.name, user.username)

        userMongoCollection.find(filter).firstOrNull()
            ?: throw UserNotFoundException()

        val update = Updates.combine(
            Updates.set(User::password.name, hashedPassword),
            Updates.set(User::userRole.name, user.userRole),
        )

        userMongoCollection.updateOne(filter, update)

    }

    override suspend fun getUserByUserName(username: String): User? {
        return userMongoCollection.find(Filters.eq(User::username.name, username)).firstOrNull()
    }


    override suspend fun isUserNameExists(username: String) {
        if (userMongoCollection.find(Filters.eq(User::username.name, username)).firstOrNull() != null) {
            throw UserNameAlreadyExistsException()
        }
    }

    override suspend fun getCurrentUser(): UserDto? {
        return userMongoCollection.find().firstOrNull()
    }

    override suspend fun getUserById(id: UUID): UserDto? {
        return userMongoCollection.find(Filters.eq(USER_ID, id)).firstOrNull()
    override suspend fun getUserById(id: UUID): User? {
        return userMongoCollection.find(Filters.eq(User::userId.name, id)).firstOrNull()
    }

}