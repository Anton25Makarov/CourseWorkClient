package by.bsuir.course.window.show;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminShowSportsmenWindow extends JFrame {
    private JPanel panel;
    private JButton backButton;
    private JTable table;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;

    public AdminShowSportsmenWindow(JFrame parent, Socket socket,
                                    ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                    java.util.List<Referee> referees, List<Sportsman> sportsmen) {

        super("Админ: меню");
        setSize(650, 460);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

    }

    private void init() {
        panel = new JPanel();
        panel.setLayout(null);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        JLabel title = new JLabel("Спортсмены");
        title.setLocation(100, 10);
        title.setSize(120, 30);

        table = new JTable(getData(), getHeaders());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setLocation(10, 50);
        scrollPane.setSize(600, 300);

        panel.add(backButton);
        panel.add(scrollPane);
        panel.add(title);

        add(panel);
    }

    private String[][] getData() {
        String[][] strings = new String[sportsmen.size()][5];
        for (int i = 0; i < strings.length; i++) {
            strings[i][0] = sportsmen.get(i).getName();
            strings[i][1] = sportsmen.get(i).getSurname();
            strings[i][2] = sportsmen.get(i).getAddress().getCountry();
            strings[i][3] = sportsmen.get(i).getAddress().getCity();
            strings[i][4] = sportsmen.get(i).getPerformance().getName();
        }

        return strings;
    }

    private String[] getHeaders() {
        String[] strings = {"Имя", "Фамилия", "Страна", "Город", "Спорт"};
        return strings;
    }
}
