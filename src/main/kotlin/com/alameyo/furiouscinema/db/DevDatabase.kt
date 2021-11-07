package com.alameyo.furiouscinema.db

import com.mongodb.client.MongoDatabase
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class DevDatabase : DatabaseConfiguration() {
    override fun database(): MongoDatabase = mongoClient.getDatabase("furious")
}
