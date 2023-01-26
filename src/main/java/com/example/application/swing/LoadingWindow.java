package com.example.application.swing;

import com.example.application.App;
import com.osiris.autoplug.core.logger.AL;
import com.osiris.betterlayout.BLayout;
import me.friwi.jcefmaven.EnumProgress;
import me.friwi.jcefmaven.IProgressHandler;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadingWindow extends JFrame {
    BLayout rootLayout;
    JLabel txtStatus = Swing.transparent(new JLabel("Loading..."));

    public LoadingWindow() throws HeadlessException, IOException {
        super(App.name);
        setIconImage(App.getIcon());
        setVisible(false);
        setUndecorated(true);
        this.rootLayout = new BLayout(this, true);
        this.setContentPane(rootLayout);
        setBackground(Color.white);

        Image icon = App.getIcon();
        JPanel title = Swing.transparent(new JPanel());
        rootLayout.addH(title);
        title.add(Swing.transparent(Swing.image(icon, 30, 30)));
        JLabel appName = new JLabel(App.name);
        title.add(appName);
        title.add(txtStatus);
        appName.setForeground(Color.GRAY);
        rootLayout.addV(txtStatus);
        txtStatus.setForeground(Color.GRAY);

        setSize(300, 100);
        Swing.center(this);
        Swing.roundCorners(this, 10, 10);
    }

    public IProgressHandler getProgressHandler() {
        final Logger logger = Logger.getLogger(ConsoleProgressHandler.class.getName());
        return (state, percent) -> {
            AL.info(""+state);
            if (state == EnumProgress.INITIALIZED) {
                this.setVisible(false);
                this.dispose();
            } else {
                this.setVisible(true);
            }
            Objects.requireNonNull(state, "state cannot be null");
            if (percent == -1.0F || !(percent < 0.0F) && !(percent > 100.0F)) {
                logger.log(Level.INFO, state + " |> " + (percent == -1.0F ? "" : (int) percent + "%")); // cast to int, since It's anyways always .0
            } else {
                throw new RuntimeException("percent has to be -1f or between 0f and 100f. Got " + percent + " instead");
            }
            if (this.isVisible()) {
                txtStatus.setText("JCEF dependency: " + state.name().toLowerCase() + "... " + (percent == -1.0F ? "" : (int) percent + "%"));
                //matchContentSize();
            }
        };
    }
}
