package by.bsuir.course.window;

import by.bsuir.course.entities.Referee;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.List;

public class MenuAdminWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton addButton;
    private JButton addSportsmanButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    public MenuAdminWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream,
                           ObjectInputStream objectInputStream) {
        super("Админ: меню");
        setSize(300, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        parent.setVisible(false);

        menuAdminLabel = new JLabel("Меню: ");
        menuAdminLabel.setLocation(120, 10);
        menuAdminLabel.setSize(75, 50);

        addButton = new JButton("Добавить");
        addButton.setLocation(50, 70);
        addButton.setSize(180, 30);

        addSportsmanButton = new JButton("Удалить");
        addSportsmanButton.setLocation(50, 120);
        addSportsmanButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 300);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(addSportsmanButton);
        panel.add(addButton);
        panel.add(menuAdminLabel);

        add(panel);

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

            List<Referee> referees = (List<Referee>) objectInputStream.readObject();

            System.out.println(referees);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
