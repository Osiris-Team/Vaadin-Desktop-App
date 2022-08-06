package com.example.application;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 *
 */
public class Window extends JFrame {
    private final CefApp cefApp;
    private final CefClient client;
    private final CefBrowser browser;
    private final Component browserUI;

    public Window() throws HeadlessException {
        this("http://localhost:" + App.port);
    }

    public Window(String startURL){
        this(startURL, false);
    }

    public Window(String startURL, boolean isTransparent) {
        this(startURL, isTransparent, 70, 60);
    }

    /**
     * To display a simple browser window, it suffices completely to create an
     * instance of the class CefBrowser and to assign its UI component to your
     * application (e.g. to your content pane).
     * But to be more verbose, this CTOR keeps an instance of each object on the
     * way to the browser UI.
     */
    public Window(String startURL, boolean isTransparent, int widthPercent, int heightPercent) {
        try{
            // (0) Initialize CEF using the maven loader
            CefAppBuilder builder = new CefAppBuilder();
            builder.setProgressHandler(new ConsoleProgressHandler());
            builder.getCefSettings().windowless_rendering_enabled = false;
            // USE builder.setAppHandler INSTEAD OF CefApp.addAppHandler!
            // Fixes compatibility issues with MacOSX
            builder.setAppHandler(new MavenCefAppHandlerAdapter() {
                @Override
                public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                    // Shutdown the app if the native CEF part is terminated
                    if (state == CefApp.CefAppState.TERMINATED) System.exit(0);
                }
            });

            // (1) The entry point to JCEF is always the class CefApp. There is only one
            //     instance per application and therefore you have to call the method
            //     "getInstance()" instead of a CTOR.
            //
            //     CefApp is responsible for the global CEF context. It loads all
            //     required native libraries, initializes CEF accordingly, starts a
            //     background task to handle CEF's message loop and takes care of
            //     shutting down CEF after disposing it.
            //
            //     WHEN WORKING WITH MAVEN: Use the builder.build() method to
            //     build the CefApp on first run and fetch the instance on all consecutive
            //     runs. This method is thread-safe and will always return a valid app
            //     instance.
            cefApp = builder.build();

            // (2) JCEF can handle one to many browser instances simultaneous. These
            //     browser instances are logically grouped together by an instance of
            //     the class CefClient. In your application you can create one to many
            //     instances of CefClient with one to many CefBrowser instances per
            //     client. To get an instance of CefClient you have to use the method
            //     "createClient()" of your CefApp instance. Calling an CTOR of
            //     CefClient is not supported.
            //
            //     CefClient is a connector to all possible events which come from the
            //     CefBrowser instances. Those events could be simple things like the
            //     change of the browser title or more complex ones like context menu
            //     events. By assigning handlers to CefClient you can control the
            //     behavior of the browser. See tests.detailed.MainFrame for an example
            //     of how to use these handlers.
            client = cefApp.createClient();

            // (3) Create a simple message router to receive messages from CEF.
            CefMessageRouter msgRouter = CefMessageRouter.create();
            client.addMessageRouter(msgRouter);

            // (4) One CefBrowser instance is responsible to control what you'll see on
            //     the UI component of the instance. It can be displayed off-screen
            //     rendered or windowed rendered. To get an instance of CefBrowser you
            //     have to call the method "createBrowser()" of your CefClient
            //     instances.
            //
            //     CefBrowser has methods like "goBack()", "goForward()", "loadURL()",
            //     and many more which are used to control the behavior of the displayed
            //     content. The UI is held within a UI-Compontent which can be accessed
            //     by calling the method "getUIComponent()" on the instance of CefBrowser.
            //     The UI component is inherited from a java.awt.Component and therefore
            //     it can be embedded into any AWT UI.
            browser = client.createBrowser(startURL, false, isTransparent);
            browserUI = browser.getUIComponent();

            // (6) All UI components are assigned to the default content pane of this
            //     JFrame and afterwards the frame is made visible to the user.
            getContentPane().add(browserUI, BorderLayout.CENTER);
            pack();
            width(widthPercent);
            height(heightPercent);
            setVisible(true);

            // (7) To take care of shutting down CEF accordingly, it's important to call
            //     the method "dispose()" of the CefApp instance if the Java
            //     application will be closed. Otherwise you'll get asserts from CEF.
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    CefApp.getInstance().dispose();
                    dispose();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
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
     * @param widthPercent 0 to 100% of the parent size (screen if null).
     */
    public void width(int widthPercent) {
        Objects.requireNonNull(this);
        updateWidth(this.getParent(), this, widthPercent);
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
     * @param heightPercent 0 to 100% of the parent size (screen if null).
     */
    public void height(int heightPercent) {
        updateHeight(this.getParent(), this, heightPercent);
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
