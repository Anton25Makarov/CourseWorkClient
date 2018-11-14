package by.bsuir.window;

import javax.swing.*;
import java.awt.*;

public class MenuAdminWindow extends JDialog {
    private MenuUserWindow menuUserWindow;

    public MenuAdminWindow(JFrame authorization) {
        super(authorization, "Меню", true);

        JButton ok = new JButton("ok");
        JButton startButton = new JButton("start New");

        setLayout(null);

        ok.addActionListener(e -> {
            setVisible(false);
            authorization.setVisible(true);

        });

        startButton.addActionListener((event) -> {
            setVisible(false);
            if (menuUserWindow == null) {
                menuUserWindow = new MenuUserWindow(this);
            }
            menuUserWindow.setVisible(true);
        });

        startButton.setLocation(50, 50);
        startButton.setSize(50, 50);

        ok.setLocation(50, 150);
        ok.setSize(50, 50);

        add(startButton);
        add(ok);
    }
}
