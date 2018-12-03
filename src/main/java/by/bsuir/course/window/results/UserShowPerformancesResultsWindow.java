package by.bsuir.course.window.results;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserShowPerformancesResultsWindow extends JFrame {
    private JPanel panel;
    private JPanel panelFigureSkating;
    private JPanel panelDiving;
    private JPanel panelSkiJumping;
    private JButton backButton;
    private JTable tableFigureSkating;
    private JTable tableDiving;
    private JTable tableSkiJumping;
    private JTabbedPane tabbedPane;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private Referee entryReferee;

    private List<Sportsman> sportsmenFigureSkating;
    private List<Sportsman> sportsmenDiving;
    private List<Sportsman> sportsmenSkiJumping;

    public UserShowPerformancesResultsWindow(JFrame parent, Socket socket,
                                             ObjectOutputStream objectOutputStream,
                                             ObjectInputStream objectInputStream,
                                             List<Referee> referees, List<Sportsman> sportsmen,
                                             Referee entryReferee) {

        super("Результаты выступлений");
        setSize(800, 460);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;
        this.entryReferee = entryReferee;

        parent.setVisible(false);

        readSportsmenFigureSkating();
        readSportsmenDiving();
        readSportsmenSkiJumping();

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

        tableFigureSkating = new JTable(getDataFigureSkating(), getHeaders());
        tableDiving = new JTable(getDataDiving(), getHeaders());
        tableSkiJumping = new JTable(getDataSkiJumping(), getHeaders());

        JScrollPane scrollPaneFigureSkating = new JScrollPane(tableFigureSkating);
        scrollPaneFigureSkating.setLocation(0, 0);
        scrollPaneFigureSkating.setSize(745, 335);

        JScrollPane scrollPaneDiving = new JScrollPane(tableDiving);
        scrollPaneDiving.setLocation(0, 0);
        scrollPaneDiving.setSize(745, 335);

        JScrollPane scrollPaneSkiJumping = new JScrollPane(tableSkiJumping);
        scrollPaneSkiJumping.setLocation(0, 0);
        scrollPaneSkiJumping.setSize(745, 335);



        tabbedPane = new JTabbedPane();
        panelFigureSkating = new JPanel();
        panelDiving = new JPanel();
        panelSkiJumping = new JPanel();

        panelFigureSkating.setLayout(null);
        panelFigureSkating.add(scrollPaneFigureSkating);

        panelDiving.setLayout(null);
        panelDiving.add(scrollPaneDiving);

        panelSkiJumping.setLayout(null);
        panelSkiJumping.add(scrollPaneSkiJumping);

        tabbedPane.addTab("Фигурное катание", panelFigureSkating);
        tabbedPane.addTab("Дайвинг", panelDiving);
        tabbedPane.addTab("Прыжки с трамплина", panelSkiJumping);


        panel.add(backButton);
        panel.add(tabbedPane);

        getContentPane().add(tabbedPane, null);
        tabbedPane.setSize(780, 350);
        tabbedPane.setLocation(0, 0);

        add(panel);
    }


    private void readSportsmenFigureSkating() {
        sportsmenFigureSkating = new ArrayList<>();
        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getName().equals("Фигурное катание") &&
                    !sportsman.getPerformance().getMarks().isEmpty() &&
                    sportsman.getPerformance().getMarks().size() == sportsman.getPerformance().getCountOfReferees()) {
                sportsmenFigureSkating.add(sportsman);
            }
        }
    }

    private void readSportsmenDiving() {
        sportsmenDiving = new ArrayList<>();
        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getName().equals("Дайвинг") &&
                    !sportsman.getPerformance().getMarks().isEmpty() &&
                    sportsman.getPerformance().getMarks().size() == sportsman.getPerformance().getCountOfReferees()) {
                sportsmenDiving.add(sportsman);
            }
        }
    }

    private void readSportsmenSkiJumping() {
        sportsmenSkiJumping = new ArrayList<>();
        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getName().equals("Прыжки с трамплина") &&
                    !sportsman.getPerformance().getMarks().isEmpty() &&
                    sportsman.getPerformance().getMarks().size() == sportsman.getPerformance().getCountOfReferees()) {
                sportsmenSkiJumping.add(sportsman);
            }
        }
    }


    private String[][] getDataFigureSkating() {
        if (sportsmenFigureSkating.isEmpty()) {
            return new String[0][0];
        }

        String[][] strings = new String[sportsmenFigureSkating.size()][4];
        for (int i = 0; i < strings.length; i++) {
            strings[i][0] = sportsmenFigureSkating.get(i).getName();
            strings[i][1] = sportsmenFigureSkating.get(i).getSurname();
            strings[i][2] = sportsmenFigureSkating.get(i).getPerformance().getName();
//            strings[i][3] = String.valueOf(sportsmenFigureSkating.get(i).calculateMark());
            strings[i][3] = String.format("%(.2f", sportsmenFigureSkating.get(i).calculateMark());
        }
        return strings;
    }

    private String[] getHeaders() {
        return new String[]{"Имя", "Фамилия", "Спорт", "Итоговая оценка"};
    }

    private String[][] getDataDiving() {
        if (sportsmenDiving.isEmpty()) {
            return new String[0][0];
        }
        String[][] strings = new String[sportsmenDiving.size()][4];
        for (int i = 0; i < strings.length; i++) {
            strings[i][0] = sportsmenDiving.get(i).getName();
            strings[i][2] = sportsmenDiving.get(i).getPerformance().getName();
            strings[i][1] = sportsmenDiving.get(i).getSurname();
//            strings[i][3] = String.valueOf(sportsmenDiving.get(i).calculateMark());
            strings[i][3] = String.format("%(.2f", sportsmenDiving.get(i).calculateMark());

        }

        return strings;
    }

    private String[][] getDataSkiJumping() {
        if (sportsmenSkiJumping.isEmpty()) {
            return new String[0][0];
        }

        String[][] strings = new String[sportsmenSkiJumping.size()][4];
        for (int i = 0; i < strings.length; i++) {
            strings[i][1] = sportsmenSkiJumping.get(i).getSurname();
            strings[i][0] = sportsmenSkiJumping.get(i).getName();
            strings[i][2] = sportsmenSkiJumping.get(i).getPerformance().getName();
//            strings[i][3] = String.valueOf(sportsmenSkiJumping.get(i).calculateMark());
            strings[i][3] = String.format("%(.2f", sportsmenSkiJumping.get(i).calculateMark());

        }
        return strings;
    }
}
