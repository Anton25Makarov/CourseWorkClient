package by.bsuir.course.window.remove;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminRemoveWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton removeSportsmanButton;
    private JButton removeRefereeButton;
    private JButton removePerformanceButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminRemoveWindow(JFrame parent, Socket socket,
                             ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                             List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: удаление");
        setSize(300, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        removeRefereeButton.addActionListener(event -> {
            AdminRemoveRefereeWindow adminRemoveRefereeWindow =
                    new AdminRemoveRefereeWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminRemoveRefereeWindow.setVisible(true);
            adminRemoveRefereeWindow.setLocationRelativeTo(null);
        });

        removeSportsmanButton.addActionListener(event -> {
            AdminRemoveSportsmanWindow adminRemoveSportsmanWindow =
                    new AdminRemoveSportsmanWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminRemoveSportsmanWindow.setVisible(true);
            adminRemoveSportsmanWindow.setLocationRelativeTo(null);
        });

        removePerformanceButton.addActionListener(event -> {
            AdminRemovePerformanceWindow adminRemovePerformanceWindow =
                    new AdminRemovePerformanceWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminRemovePerformanceWindow.setVisible(true);
            adminRemovePerformanceWindow.setLocationRelativeTo(null);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

        saveSportsmenItem.addActionListener(event -> {
            try {
                objectOutputStream.flush();
                objectOutputStream.reset();

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

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Сохранить");
        menuBar.add(menu);

        saveSportsmenItem = new JMenuItem("Сохранить всё");
        menu.add(saveSportsmenItem);

        menuAdminLabel = new JLabel("Удаление: ");
        menuAdminLabel.setLocation(100, 10);
        menuAdminLabel.setSize(100, 50);

        removeSportsmanButton = new JButton("Удалить спортсмена");
        removeSportsmanButton.setLocation(50, 70);
        removeSportsmanButton.setSize(180, 30);

        removeRefereeButton = new JButton("Удалить судью");
        removeRefereeButton.setLocation(50, 120);
        removeRefereeButton.setSize(180, 30);

        removePerformanceButton = new JButton("Удалить выступление");
        removePerformanceButton.setLocation(50, 170);
        removePerformanceButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 220);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(removePerformanceButton);
        panel.add(removeRefereeButton);
        panel.add(removeSportsmanButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
