package com.alameyo.furiouscinema.db

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
abstract class DatabaseConfiguration {
    protected val mongoClient = MongoClients.create()
    @Bean
    abstract fun database(): MongoDatabase
}
