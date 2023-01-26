package com.example.application;

import com.example.application.swing.DevToolsDialog;
import com.example.application.swing.LoadingWindow;
import com.example.application.swing.Window;
import com.osiris.autoplug.core.logger.AL;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "mytodo") // TODO customize themes in /frontend
@PWA(name = "My Todo", shortName = "My Todo", offlineResources = {}) // TODO enter app details here
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class App extends SpringBootServletInitializer implements AppShellConfigurator {
    public static final String name = "My Todo";
    public static ConfigurableApplicationContext context;
    public static com.example.application.swing.Window window;
    public static int port;
    /**
     * Should be the directory in which this application was started. <br>
     * Can be used to store information that is not specific to an user. <br>
     */
    public static final File workingDir = new File(System.getProperty("user.dir"));
    /**
     * Should get cleared by the operating system on reboots. <br>
     * Example on Windows: <br>
     * C:\Users\UserName\AppData\Local\Temp\AppName
     */
    public static final File tempDir = new File(System.getProperty("java.io.tmpdir") + "/" + name);
     /**
     * Can be used to store user-specific data. <br>
     * Example on Windows: <br>
     * C:\Users\UserName\AppName
     */
    public static final File userDir = new File(System.getProperty("user.home") + "/" + name);

    public static void main(String[] args) throws IOException {
        if (!AL.isStarted) {
            AL.start("Logger", true, new File(workingDir + "/latest.log"), false);
            AL.mirrorSystemStreams(new File(workingDir + "/mirror-out.log"), new File(workingDir + "/mirror-err.log"));
        }
        AL.info("Starting application...");
        AL.info("workingDir = " + workingDir);
        AL.info("tempDir = " + tempDir);
        AL.info("userDir = " + userDir);
        LoadingWindow loadingWin = new LoadingWindow();
        loadingWin.setVisible(true);
        SpringApplication springApp = new SpringApplication(App.class);

        Properties props = new Properties();
        port = new ServerSocket(0).getLocalPort(); // get random free port
        props.put("server.port", port);
        props.put("security.require-ssl", "false");
        springApp.setDefaultProperties(props);
        AL.info("Set default SpringBoot properties to: " + props);
        context = springApp.run(args);
        AL.info("SpringBoot context initialized.");
        System.setProperty("java.awt.headless", "false");
        window = new Window();
        new DevToolsDialog("DevTools", window.browser).setVisible(true); // TODO remove devtools in production
        AL.info("Created main window.");
        AL.info("Started application successfully!");
        loadingWin.close();
    }

    public static Image getIcon() throws IOException {
        return getResourceImage("/icons/icon.png");
    }

    /**
     * @param path expected to be child of /META-INF/resources. Example: icon.png or /icon.png
     */
    public static Image getResourceImage(String path) throws IOException {
        String defaultPath = "/META-INF/resources";
        String fullPath = defaultPath + (path.startsWith("/") ? path : "/" + path);
        File img = new File(App.workingDir + fullPath);
        if (!img.exists()) {
            img.getParentFile().mkdirs();
            img.createNewFile();
            InputStream link = (App.class.getResourceAsStream(fullPath));
            Files.copy(link, img.toPath(), StandardCopyOption.REPLACE_EXISTING);
            link.close();
        }
        return Toolkit.getDefaultToolkit().getImage(img.getAbsolutePath());
    }
}
