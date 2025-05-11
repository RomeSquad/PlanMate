package org.example.data.datasource.authentication

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.toList
import org.example.logic.entity.auth.User

class MongoAuthDataSource(
    val mongo: MongoCollection<User>
) : AuthDataSource {
    override suspend fun getAllUsers(): List<User> {
        return mongo.find().toList()
    }

    override suspend fun saveAllUsers(users: List<User>) {
        mongo.insertMany(users)
    }
}