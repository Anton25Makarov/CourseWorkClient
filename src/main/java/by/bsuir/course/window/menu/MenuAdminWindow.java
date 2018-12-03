package by.bsuir.course.window.menu;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import by.bsuir.course.window.add.AdminAddWindow;
import by.bsuir.course.window.edit.AdminEditWindow;
import by.bsuir.course.window.remove.AdminRemoveWindow;
import by.bsuir.course.window.show.AdminShowSportsmenWindow;
import by.bsuir.course.window.show.AdminShowWindow;

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
    private JButton showButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;

    public MenuAdminWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream,
                           ObjectInputStream objectInputStream) {
        super("Админ: меню");
        setSize(300, 380);
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

        changeButton.addActionListener(event -> {
            AdminEditWindow adminEditWindow =
                    new AdminEditWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminEditWindow.setVisible(true);
            adminEditWindow.setLocationRelativeTo(null);
        });

        showButton.addActionListener(event -> {
            AdminShowWindow adminShowWindow =
                    new AdminShowWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminShowWindow.setVisible(true);
            adminShowWindow.setLocationRelativeTo(null);
        });


        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

        saveSportsmenItem.addActionListener(event -> {
            try {
                objectOutputStream.writeObject("setAll");
                objectOutputStream.writeObject(null);

                objectOutputStream.writeObject(sportsmen);
                objectOutputStream.writeObject(referees);

                String result = (String) objectInputStream.readObject();
                switch (result) {
                    case "successful inserting all":
                        JOptionPane.showMessageDialog(this, "Сохранение выполнено");
                        break;
                    case "false":
                        JOptionPane.showMessageDialog(this, "Сохранение не выполнено");
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Сохранить");
        menuBar.add(menu);

        saveSportsmenItem = new JMenuItem("Сохранить всё");
        menu.add(saveSportsmenItem);

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

        showButton = new JButton("Посмотреть");
        showButton.setLocation(50, 220);
        showButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 270);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(changeButton);
        panel.add(backButton);
        panel.add(deleteButton);
        panel.add(addButton);
        panel.add(menuAdminLabel);
        panel.add(showButton);

        add(panel);
    }
}
