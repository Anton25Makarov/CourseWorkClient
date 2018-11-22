package by.bsuir.course.window;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminAddWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton addSportsmanButton;
    private JButton addRefereeButton;
    private JButton addPerformanceButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminAddWindow(JFrame parent, Socket socket,
                          ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                          List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: меню");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        addRefereeButton.addActionListener(event->{
            System.out.println("Hello =)");
        });

        addSportsmanButton.addActionListener(event -> {
            AdminAddSportsmanWindow adminAddSportsmanWindow =
                    new AdminAddSportsmanWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminAddSportsmanWindow.setVisible(true);
            adminAddSportsmanWindow.setLocationRelativeTo(null);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        menuAdminLabel = new JLabel("Добавление: ");
        menuAdminLabel.setLocation(100, 10);
        menuAdminLabel.setSize(100, 50);

        addSportsmanButton = new JButton("Добавить спортсмена");
        addSportsmanButton.setLocation(50, 70);
        addSportsmanButton.setSize(180, 30);

        addRefereeButton = new JButton("Добавить судью");
        addRefereeButton.setLocation(50, 120);
        addRefereeButton.setSize(180, 30);

        addPerformanceButton = new JButton("Добавить выступление");
        addPerformanceButton.setLocation(50, 170);
        addPerformanceButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 220);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(addPerformanceButton);
        panel.add(addRefereeButton);
        panel.add(addSportsmanButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
