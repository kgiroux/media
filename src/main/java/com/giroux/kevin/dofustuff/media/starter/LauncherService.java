package com.giroux.kevin.dofustuff.media.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.giroux.kevin.dofustuff" })

@EnableDiscoveryClient
public class LauncherService {

    /**
     * Chargement du port depuis un fichier de configuration
     */
    @Value("${server.port}")
    private static int serverPort;


    
    /**
     * Lance l'application spring boot
     * 
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) {
        SpringApplication.run(LauncherService.class, args);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(serverPort);
        return factory;
    }
	
}
