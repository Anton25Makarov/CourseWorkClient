package by.bsuir.course.window.show;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminShowWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton showSportsmanButton;
    private JButton showRefereeButton;
    private JButton showPerformanceButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminShowWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                           List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: меню");
        setSize(300, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        showRefereeButton.addActionListener(event -> {
            AdminShowRefereesWindow adminShowRefereesWindow =
                    new AdminShowRefereesWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminShowRefereesWindow.setVisible(true);
            adminShowRefereesWindow.setLocationRelativeTo(null);
        });

        showSportsmanButton.addActionListener(event -> {
            AdminShowSportsmenWindow adminShowSportsmenWindow =
                    new AdminShowSportsmenWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminShowSportsmenWindow.setVisible(true);
            adminShowSportsmenWindow.setLocationRelativeTo(null);
        });

        showPerformanceButton.addActionListener(event -> {
            AdminShowPerformancesWindow adminShowPerformancesWindow =
                    new AdminShowPerformancesWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminShowPerformancesWindow.setVisible(true);
            adminShowPerformancesWindow.setLocationRelativeTo(null);
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

        menuAdminLabel = new JLabel("Просмотр: ");
        menuAdminLabel.setLocation(100, 10);
        menuAdminLabel.setSize(100, 50);

        showSportsmanButton = new JButton("Посмотреть спортсменов");
        showSportsmanButton.setLocation(50, 70);
        showSportsmanButton.setSize(180, 30);

        showRefereeButton = new JButton("Посмотреть судей");
        showRefereeButton.setLocation(50, 120);
        showRefereeButton.setSize(180, 30);

        showPerformanceButton = new JButton("Посмотреть выступления");
        showPerformanceButton.setLocation(50, 170);
        showPerformanceButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 220);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);


        panel.add(backButton);
        panel.add(showPerformanceButton);
        panel.add(showRefereeButton);
        panel.add(showSportsmanButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
