package org.example.data.datasource.authentication

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.request.auth.CreateUserRequest
import org.example.data.datasource.mapper.toUser
import org.example.logic.entity.auth.User
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.auth.LoginRequest
import org.example.utils.hashPassword

class MongoAuthDataSource(
    private val userMongoCollection: MongoCollection<User>
) : AuthDataSource {
    override suspend fun getAllUsers(): List<User> {
        return userMongoCollection.find().toList()
    }

    override suspend fun insertUser(request: CreateUserRequest): User {
        isUserNameExists(request.username)
        val newUser = request.toUser()
        userMongoCollection.insertOne(newUser)
        return newUser
    }

    override suspend fun loginUser(request: LoginRequest): User {
        val hashedPassword = hashPassword(request.password)
        val filter = Filters.and(
            Filters.eq("username", request.username),
            Filters.eq("password", hashedPassword)
        )
        val user = userMongoCollection
            .find(filter)
            .firstOrNull()
            ?: throw UserNotFoundException()
        return user
    }

    override suspend fun deleteUser(username: String): Boolean {
        val filter = Filters.eq("username", username)
        val deleteResult = userMongoCollection
            .deleteOne(filter)
        return if (deleteResult.wasAcknowledged()) {
            true
        } else {
            throw UserNotFoundException()
        }
    }

    override suspend fun editUser(user: User) {
        val hashedPassword = hashPassword(user.password)

        val filter = Filters.eq("username", user.username)

        userMongoCollection.find(filter).firstOrNull()
            ?: throw UserNotFoundException()

        val update = Updates.combine(
            Updates.set("password", hashedPassword),
            Updates.set("userRole", user.userRole),
        )

        userMongoCollection.updateOne(filter, update)

    }

    override suspend fun getUserByUserName(username: String): User? {
        return userMongoCollection.find(Filters.eq("username", username)).firstOrNull()
    }


    override suspend fun isUserNameExists(username: String) {
        if (userMongoCollection.find(Filters.eq("username", username)).firstOrNull() != null) {
            throw UserNameAlreadyExistsException()
        }
    }

    override suspend fun getCurrentUser(): User? {
        return userMongoCollection.find().firstOrNull()
    }

}