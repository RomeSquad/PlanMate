package data.datasource

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import org.bson.Document

object MongoClientConnectionExample {
    fun main() {
        val connectionString = "mongodb+srv://<db_username>:<db_password>@plan-mate.rxaopvb.mongodb.net/?retryWrites=true&w=majority&appName=plan-mate"
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        // Create a new client and connect to the server
        MongoClient.create(mongoClientSettings).use { mongoClient ->
            val database = mongoClient.getDatabase("admin")
            runBlocking {
                database.runCommand(Document("ping", 1))
            }
            println("Pinged your deployment. You successfully connected to MongoDB!")
        }
    }
}