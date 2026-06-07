package com.lets_play.lets_play.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "letsplay";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        // On force la chaîne de connexion MongoDB Atlas directement en Java
        String connectionString = "mongodb+srv://timothee_db_user:Spiderman123@lets-play.hpklkci.mongodb.net/letsplay?retryWrites=true&w=majority";
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}