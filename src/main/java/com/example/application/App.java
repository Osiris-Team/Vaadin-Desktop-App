package com.example.application;

import com.osiris.autoplug.core.logger.AL;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.metrics.StartupStep;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "mytodo") // TODO customize themes in /frontend
@PWA(name = "My Todo", shortName = "My Todo", offlineResources = {}) // TODO enter app details here
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class App extends SpringBootServletInitializer implements AppShellConfigurator {
    public static ConfigurableApplicationContext context;
    public static Window window;
    public static int port;
    public static File workingDir = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws IOException {
        if(!AL.isStarted){
            AL.start("Logger", true, new File(workingDir + "/latest.log"), false);
            AL.mirrorSystemStreams(new File(workingDir + "/mirror-out.log"), new File(workingDir + "/mirror-err.log"));
        }
        AL.info("Starting application...");
        SpringApplication springApp = new SpringApplication(App.class);
        Properties props = new Properties();
        port = new ServerSocket(0).getLocalPort(); // get random free port
        props.put("server.port", port);
        props.put("security.require-ssl", "false");
        springApp.setDefaultProperties(props);
        AL.info("Set default SpringBoot properties to: "+props.toString());
        context = springApp.run(args);
        AL.info("SpringBoot context initialized.");
        System.setProperty("java.awt.headless", "false");
        window = new Window();
        AL.info("Created main window.");
        AL.info("Started application successfully!");
    }
}
