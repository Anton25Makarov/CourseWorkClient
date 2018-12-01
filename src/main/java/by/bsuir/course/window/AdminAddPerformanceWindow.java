package by.bsuir.course.window;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class AdminAddPerformanceWindow extends JFrame {

    private JLabel sportsmenLabel;
    private JLabel refereeLabel;

    private JButton chooseSportsmanButton;
    private JButton chooseRefereeButton;

    private JList<String> sportsmenList;
    private JList<String> refereeList;

    private Sportsman currentSportsman;
    private List<Referee> currentReferees;

    private JScrollPane scrollPaneSportsmen;
    private JScrollPane scrollPaneReferees;

    private DefaultListModel<String> listModelSportsmen;
    private DefaultListModel<String> listModelReferees;

    private DefaultListModel<String> listModelSportsmenChosen;
    private DefaultListModel<String> listModelRefereesChosen;

    private JList<String> sportsmenListChosen;
    private JList<String> refereeListChosen;

    private JButton addPerformanceButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminAddPerformanceWindow(JFrame parent, Socket socket,
                                     ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                     List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: меню");
        setSize(810, 610);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        currentReferees = new ArrayList<>();

        chooseSportsmanButton.addActionListener(e -> {
            int selectedIndex = sportsmenList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Выберите спортсмена");
                return;
            }

            String[] sportsmanParts = sportsmenList.getSelectedValue().split(" ");


            for (Sportsman sportsman : sportsmen) {
                if (sportsman.getName().equals(sportsmanParts[0]) && sportsman.getSurname().equals(sportsmanParts[1])) {
                    currentSportsman = sportsman;
                    break;
                }
            }

            listModelSportsmenChosen.clear();
            listModelSportsmenChosen.addElement(sportsmenList.getSelectedValue());

            listModelRefereesChosen.clear(); // if we change sport
            currentReferees.clear();

            listModelReferees.clear();
            readReferees();
        });

        chooseRefereeButton.addActionListener(e -> {
            if (currentSportsman == null) {
                JOptionPane.showMessageDialog(this, "Выберите спортсмена");
                return;
            }
            int selectedIndex = refereeList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Выберите Рефери");
                return;
            }

            if (listModelRefereesChosen.size() >= currentSportsman.getPerformance().getCountOfReferees()) {
                JOptionPane.showMessageDialog(this,
                        "Для данного вида спорта количество судей: " + currentSportsman.getPerformance().getCountOfReferees());
                return;
            }

            String[] refereeParts = refereeList.getSelectedValue().split(" ");

            for (Referee referee : referees) {
                if (referee.getName().equals(refereeParts[0]) &&
                        referee.getSurname().equals(refereeParts[1])) {
                    currentReferees.add(referee);
                    break;
                }
            }

            listModelRefereesChosen.addElement(refereeList.getSelectedValue());

            listModelReferees.removeElementAt(selectedIndex);
        });

        addPerformanceButton.addActionListener(e -> {
            if (currentSportsman == null) {
                JOptionPane.showMessageDialog(this, "Вы не выбрали спортсмена");
                return;
            }
            if (listModelRefereesChosen.size() != currentSportsman.getPerformance().getCountOfReferees()) {
                JOptionPane.showMessageDialog(this,
                        "Для данного вида спорта количество судей: " + currentSportsman.getPerformance().getCountOfReferees());
                return;
            }


            for (Sportsman sportsman : sportsmen) {
                if (sportsman.equals(currentSportsman)) {
                    for (Referee referee : currentReferees) {
                        sportsman.getPerformance().addResult(referee, null);
                    }
                }
            }

            currentSportsman = null;
            listModelReferees.clear();
            listModelRefereesChosen.clear();
            listModelSportsmen.clear();
            listModelSportsmenChosen.clear();
            readSportsmen();
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        panel = new JPanel();
        panel.setLayout(null);
        listModelSportsmen = new DefaultListModel<>();
        listModelReferees = new DefaultListModel<>();
        listModelSportsmenChosen = new DefaultListModel<>();
        listModelRefereesChosen = new DefaultListModel<>();

        sportsmenList = new JList<>(listModelSportsmen);
        sportsmenList.setLayoutOrientation(JList.VERTICAL);


        refereeList = new JList<>(listModelReferees);
        refereeList.setLayoutOrientation(JList.VERTICAL);


        sportsmenLabel = new JLabel("Спортсмены");
        sportsmenLabel.setLocation(100, 10);
        sportsmenLabel.setSize(100, 50);


        refereeLabel = new JLabel("Рефери");
        refereeLabel.setLocation(500, 10);
        refereeLabel.setSize(100, 50);


        backButton = new JButton("Назад");
        backButton.setLocation(10, 530);
        backButton.setSize(80, 30);

        addPerformanceButton = new JButton("Добавить");
        addPerformanceButton.setLocation(300, 480);
        addPerformanceButton.setSize(180, 50);

        /////////////////////////////////
        sportsmenListChosen = new JList<>(listModelSportsmenChosen);
        refereeListChosen = new JList<>(listModelRefereesChosen);

        sportsmenListChosen.setLocation(50, 300);
        sportsmenListChosen.setSize(300, 100);

        refereeListChosen.setLocation(400, 300);
        refereeListChosen.setSize(300, 100);

        chooseSportsmanButton = new JButton("Выбрать спортсмена");
        chooseSportsmanButton.setLocation(50, 420);
        chooseSportsmanButton.setSize(200, 40);

        chooseRefereeButton = new JButton("Выбрать рефери");
        chooseRefereeButton.setLocation(400, 420);
        chooseRefereeButton.setSize(200, 40);


        readSportsmen();

        scrollPaneSportsmen = new JScrollPane(sportsmenList);
        scrollPaneSportsmen.setLocation(50, 50);
        scrollPaneSportsmen.setSize(300, 200);

        scrollPaneReferees = new JScrollPane(refereeList);
        scrollPaneReferees.setLocation(400, 50);
        scrollPaneReferees.setSize(300, 200);


        panel.add(sportsmenLabel);
        panel.add(refereeLabel);
        panel.add(scrollPaneSportsmen);
        panel.add(scrollPaneReferees);
        panel.add(addPerformanceButton);
        panel.add(backButton);
        panel.add(sportsmenListChosen);
        panel.add(refereeListChosen);
        panel.add(chooseSportsmanButton);
        panel.add(chooseRefereeButton);


        add(panel);
    }

    private void readSportsmen() {

        for (Sportsman sportsman : sportsmen) {
            if (sportsman.getPerformance().getMarks().isEmpty()) {
                listModelSportsmen.addElement(sportsman.getName() + " " +
                        sportsman.getSurname() + " - " +
                        sportsman.getPerformance().getName());
            }
        }
    }

    private void readReferees() {

        for (Referee referee : referees) {
            if (referee.getSport().equals(currentSportsman.getPerformance().getName())) {
                listModelReferees.addElement(referee.getName() + " " +
                        referee.getSurname() + " - " +
                        referee.getSport());
            }
        }
    }
}
