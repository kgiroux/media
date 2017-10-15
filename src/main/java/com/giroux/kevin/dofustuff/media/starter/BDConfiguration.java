package com.giroux.kevin.dofustuff.media.starter;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration Spring pour la connexion à la BD
 * 
 * @author Kevin Giroux
 *
 */
@Configuration
@EnableJpaRepositories({ "com.giroux.kevin.dofustuff.*" })
@EntityScan({ "com.giroux.kevin.dofustuff.media.persistence.entity" })
public class BDConfiguration{

}
