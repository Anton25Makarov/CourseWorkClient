package by.bsuir.course.window.show;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdminShowPerformancesWindow extends JFrame {
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

    public AdminShowPerformancesWindow(JFrame parent, Socket socket,
                                       ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                       List<Referee> referees, List<Sportsman> sportsmen) {

        super("Админ: меню");
        setSize(800, 460);
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

        tableFigureSkating = new JTable(getDataFigureSkating(), getHeadersFigureSkating());
        tableDiving = new JTable(getDataDiving(), getHeadersDiving());
        tableSkiJumping = new JTable(getDataSkiJumping(), getHeadersSkiJumping());

        JScrollPane scrollPaneFigureSkating = new JScrollPane(tableFigureSkating);
        scrollPaneFigureSkating.setLocation(0, 0);
        scrollPaneFigureSkating.setSize(745, 340);

        JScrollPane scrollPaneDiving = new JScrollPane(tableDiving);
        scrollPaneDiving.setLocation(0, 0);
        scrollPaneDiving.setSize(745, 340);

        JScrollPane scrollPaneSkiJumping = new JScrollPane(tableSkiJumping);
        scrollPaneSkiJumping.setLocation(0, 0);
        scrollPaneSkiJumping.setSize(745, 340);


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
        tabbedPane.setLocation(0, 0);
        tabbedPane.setSize(750, 350);

        add(panel);
    }

    private String[][] getDataFigureSkating() {
        List<Sportsman> sportsmenFigureSkating = new ArrayList<>();
        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getName().equals("Фигурное катание") &&
                    !sportsman.getPerformance().getMarks().isEmpty()) {
                sportsmenFigureSkating.add(sportsman);
            }
        }
        if (sportsmenFigureSkating.isEmpty()) {
            return new String[0][0];
        }
        int countOfRef = sportsmenFigureSkating.get(0).getPerformance().getCountOfReferees();
        final int countOfCol = 3 + 2 * countOfRef;
        String[][] strings = new String[sportsmenFigureSkating.size()][countOfCol];
        for (int i = 0; i < strings.length; i++) {
            List<Mark> marks = new ArrayList<>(sportsmenFigureSkating.get(i).getPerformance().getMarks().values());

            strings[i][0] = sportsmenFigureSkating.get(i).getName();
            strings[i][1] = sportsmenFigureSkating.get(i).getSurname();
            strings[i][2] = sportsmenFigureSkating.get(i).getPerformance().getName();
            for (int j = 0, k = 3; j < countOfRef; j++, k++) {
                if (marks.get(j) == null) {
                    strings[i][k] = " - ";
                    strings[i][++k] = " - ";
                } else {
//                    strings[i][k] = String.valueOf(((FigureSkatingMark) marks.get(j)).getPresentationMark());
//                    strings[i][++k] = String.valueOf(((FigureSkatingMark) marks.get(j)).getTechnicalMark());
                    strings[i][k] = String.format("%(.2f", ((FigureSkatingMark) marks.get(j)).getPresentationMark());
                    strings[i][++k] = String.format("%(.2f", ((FigureSkatingMark) marks.get(j)).getTechnicalMark());

                }
            }

        }

        return strings;
    }

    private String[] getHeadersFigureSkating() {
        return new String[]{"Имя", "Фамилия", "Спорт", "Судья 1 (За технику)",
                "Судья 1 (За артистизм)", "Судья 2 (За технику)", "Судья 2 (За артистизм)"};
    }

    private String[][] getDataDiving() {
        List<Sportsman> sportsmenDiving = new ArrayList<>();
        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getName().equals("Дайвинг") &&
                    !sportsman.getPerformance().getMarks().isEmpty()) {
                sportsmenDiving.add(sportsman);
            }
        }
        if (sportsmenDiving.isEmpty()) {
            return new String[0][0];
        }
        int countOfRef = sportsmenDiving.get(0).getPerformance().getCountOfReferees();
        final int countOfCol = 3 + countOfRef;
        String[][] strings = new String[sportsmenDiving.size()][countOfCol];
        for (int i = 0; i < strings.length; i++) {
            List<Mark> marks = new ArrayList<>(sportsmenDiving.get(i).getPerformance().getMarks().values());

            strings[i][0] = sportsmenDiving.get(i).getName();
            strings[i][1] = sportsmenDiving.get(i).getSurname();
            strings[i][2] = sportsmenDiving.get(i).getPerformance().getName();
            for (int j = 0, k = 3; j < countOfRef; j++, k++) {
                if (marks.get(j) == null) {
                    strings[i][k] = " - ";
                } else {
//                    strings[i][k] = String.valueOf(((DivingMark) marks.get(j)).getMark());
                    strings[i][k] = String.format("%(.2f", ((DivingMark) marks.get(j)).getMark());

                }
            }
        }

        return strings;
    }

    private String[] getHeadersDiving() {
        return new String[]{"Имя", "Фамилия", "Спорт", "Судья 1",
                "Судья 2", "Судья 3"};
    }

    private String[][] getDataSkiJumping() {
        List<Sportsman> sportsmenSkiJumping = new ArrayList<>();
        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getName().equals("Прыжки с трамплина") &&
                    !sportsman.getPerformance().getMarks().isEmpty()) {
                sportsmenSkiJumping.add(sportsman);
            }
        }
        if (sportsmenSkiJumping.isEmpty()) {
            return new String[0][0];
        }
        int countOfRef = sportsmenSkiJumping.get(0).getPerformance().getCountOfReferees();
        final int countOfCol = 3 + countOfRef;
        String[][] strings = new String[sportsmenSkiJumping.size()][countOfCol];
        for (int i = 0; i < strings.length; i++) {
            List<Mark> marks = new ArrayList<>(sportsmenSkiJumping.get(i).getPerformance().getMarks().values());

            strings[i][0] = sportsmenSkiJumping.get(i).getName();
            strings[i][1] = sportsmenSkiJumping.get(i).getSurname();
            strings[i][2] = sportsmenSkiJumping.get(i).getPerformance().getName();
            for (int j = 0, k = 3; j < countOfRef; j++, k++) {
                if (marks.get(j) == null) {
                    strings[i][k] = " - ";
                } else {
//                    strings[i][k] = String.valueOf(((SkiJumpingMark) marks.get(j)).getMark());
                    strings[i][k] = String.format("%(.2f", ((SkiJumpingMark) marks.get(j)).getMark());
                }
            }
        }

        return strings;
    }

    private String[] getHeadersSkiJumping() {
        return new String[]{"Имя", "Фамилия", "Спорт", "Судья 1",
                "Судья 2", "Судья 3", "Судья 4"};
    }
}
