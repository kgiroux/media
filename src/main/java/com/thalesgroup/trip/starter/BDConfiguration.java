package com.thalesgroup.trip.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuration Spring pour la connexion à la BD
 * 
 * @author thales
 *
 */
@Configuration
@EnableMongoRepositories({ "com.thalesgroup.trip" })
public class BDConfiguration{

}
