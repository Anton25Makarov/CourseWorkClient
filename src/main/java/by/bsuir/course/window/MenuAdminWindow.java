package by.bsuir.course.window;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import by.bsuir.course.window.add.AdminAddWindow;
import by.bsuir.course.window.remove.AdminRemoveWindow;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.List;

public class MenuAdminWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton changeButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;

    public MenuAdminWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream,
                           ObjectInputStream objectInputStream) {
        super("Админ: меню");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        parent.setVisible(false);

        init();

        addButton.addActionListener(event -> {
            AdminAddWindow adminAddWindow =
                    new AdminAddWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminAddWindow.setVisible(true);
            adminAddWindow.setLocationRelativeTo(null);
        });

        deleteButton.addActionListener(event -> {
            AdminRemoveWindow adminRemoveWindow =
                    new AdminRemoveWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminRemoveWindow.setVisible(true);
            adminRemoveWindow.setLocationRelativeTo(null);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

        loadAllFromServer();
    }

    private void loadAllFromServer() {
        try {
            objectOutputStream.writeObject("getAll");

            objectOutputStream.writeObject(null);

            referees = (List<Referee>) objectInputStream.readObject();

            sportsmen = (List<Sportsman>) objectInputStream.readObject();

            System.out.println(referees);
            System.out.println(sportsmen);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        menuAdminLabel = new JLabel("Меню: ");
        menuAdminLabel.setLocation(120, 10);
        menuAdminLabel.setSize(75, 50);

        addButton = new JButton("Добавить");
        addButton.setLocation(50, 70);
        addButton.setSize(180, 30);

        deleteButton = new JButton("Удалить");
        deleteButton.setLocation(50, 120);
        deleteButton.setSize(180, 30);

        changeButton = new JButton("Изменить");
        changeButton.setLocation(50, 170);
        changeButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 220);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(changeButton);
        panel.add(backButton);
        panel.add(deleteButton);
        panel.add(addButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
