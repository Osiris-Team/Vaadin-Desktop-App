package com.example.application.swing;

import com.equo.chromium.ChromiumBrowser;
import com.example.application.App;
import com.osiris.autoplug.core.logger.AL;
import net.lingala.zip4j.ZipFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 */
public class Window {
    public final ChromiumBrowser chromiumBrowser;

    public Window() throws HeadlessException {
        this("http://localhost:" + App.port);
    }

    public Window(String startURL) {
        this(startURL, false);
    }

    public Window(String startURL, boolean isTransparent) {
        this(startURL, isTransparent, 70, 60);
    }

    public volatile boolean isRunning = true;

    /**
     * To display a simple browser window, it suffices completely to create an
     * instance of the class CefBrowser and to assign its UI component to your
     * application (e.g. to your content pane).
     * But to be more verbose, this CTOR keeps an instance of each object on the
     * way to the browser UI.
     */
    public Window(String startURL, boolean isTransparent, int widthPercent, int heightPercent) {
        try {
            lol();
            AL.info(System.getProperty("chromium.path"));
            this.chromiumBrowser = ChromiumBrowser.standalone(startURL);
            ChromiumBrowser.startBrowsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void lol() throws IOException {
        CodeSource src = Window.class.getProtectionDomain().getCodeSource();
        Objects.requireNonNull(src);
        URL jar = src.getLocation();
        ZipInputStream zip = new ZipInputStream(jar.openStream());
        while(true) {
            ZipEntry e = zip.getNextEntry();
            if (e == null)
                break;
            String name = e.getName();
            if (name.startsWith("BOOT-INF/lib/com.equo.chromium.cef")) {
                AL.info("name="+name);
                InputStream link = (getClass().getResourceAsStream("/"+name));
                File chromiumJar = new File(System.getProperty("user.dir")+ "/"+
                        name.substring(name.lastIndexOf("/") + 1));
                AL.info("newFile="+chromiumJar.toString());
                if(!chromiumJar.exists()){
                    Files.copy(link, chromiumJar.toPath());
                    new ZipFile(chromiumJar).extractAll(System.getProperty("user.dir"));
                }

                System.setProperty("chromium.path", System.getProperty("user.dir"));
            }
        }
    }

    /**
     * @see #width(int)
     */
    public void widthFull() {
        width(100);
    }

    /**
     * This invalidates the container and thus to see changes in the UI
     * make sure execute {@link Component#revalidate()} manually.
     *
     * @param widthPercent 0 to 100% of the parent size (screen if null).
     */
    public void width(int widthPercent) {
        //updateWidth(this.getParent(), this, widthPercent);
    }

    /**
     * @see #height(int)
     */
    public void heightFull() {
        height(100);
    }

    /**
     * This invalidates the container and thus to see changes in the UI
     * make sure execute {@link Component#revalidate()} manually.
     *
     * @param heightPercent 0 to 100% of the parent size (screen if null).
     */
    public void height(int heightPercent) {
        //updateHeight(this.getParent(), this, heightPercent);
    }

    private void updateWidth(Component parent, Component target, int widthPercent) {
        int parentWidth; // If no parent provided use the screen dimensions
        if (parent != null) parentWidth = parent.getWidth();
        else parentWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        Dimension size = new Dimension(parentWidth / 100 * widthPercent, target.getHeight());
        target.setSize(size);
        target.setPreferredSize(size);
        target.setMaximumSize(size);
    }

    private void updateHeight(Component parent, Component target, int heightPercent) {
        int parentHeight; // If no parent provided use the screen dimensions
        if (parent != null) parentHeight = parent.getHeight();
        else parentHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        Dimension size = new Dimension(target.getWidth(), parentHeight / 100 * heightPercent);
        target.setSize(size);
        target.setPreferredSize(size);
        target.setMaximumSize(size);
    }
}
