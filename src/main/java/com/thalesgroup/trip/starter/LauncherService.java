package com.thalesgroup.trip.starter;

import java.io.File;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.thalesgroup.trip" })
@EnableDiscoveryClient
@EnableConfigurationProperties(StorageProperties.class)
public class LauncherService {

    /**
     * Chargement du port depuis un fichier de configuration
     */
    @Value("${server.port}")
    private int SERVER_PORT;

    
    /**
     * Folder location for storing files
     */
	@Value("${media.upload.dir}")
    private String location;
    
    /**
     * Lance l'application spring boot
     * 
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        SpringApplication.run(LauncherService.class, args);
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        File tempLocation = new File(location);
        factory.setBaseDirectory(tempLocation);
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        factory.setPort(SERVER_PORT);
        return factory;
    }
	
}
