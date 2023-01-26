package com.example.application.swing;

import com.example.application.App;
import com.osiris.betterlayout.BLayout;

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
}
