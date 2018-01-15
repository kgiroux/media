package com.giroux.kevin.dofustuff.media.starter;

import org.springframework.boot.autoconfigure.web.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Classe permettant de réaliser la configuration Spring (remplacement du
 * fichier xml)
 * 
 * @author girouxkevin
 *
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(MultipartProperties.class)

public class DefaultConfiguration {
}
