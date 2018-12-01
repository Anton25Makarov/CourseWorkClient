package by.bsuir.course.window.remove;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminRemovePerformanceWindow extends JFrame {

    private JLabel sportsmenLabel;
    private JList<String> sportsmenList;
    private JScrollPane scrollPaneSportsmen;
    private DefaultListModel<String> listModelSportsmen;

    private JButton removeSportsmanButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminRemovePerformanceWindow(JFrame parent, Socket socket,
                                        ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                        List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: меню");
        setSize(400, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        removeSportsmanButton.addActionListener(e -> {
            int selectedIndex = sportsmenList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Укажите спортсмена");
                return;
            }

//            Sportsman chosenSportsman = sportsmen.get(selectedIndex);

            String[] sportsmanParts = sportsmenList.getSelectedValue().split(" ");

            Sportsman chosenSportsman = null;

            for (Sportsman sportsman : sportsmen) {
                if (sportsman.getName().equals(sportsmanParts[0]) && sportsman.getSurname().equals(sportsmanParts[1])) {
                    chosenSportsman = sportsman;
                    break;
                }
            }
            int answer = JOptionPane.showConfirmDialog(this,
                    "Вы действительно хотите удалить выступление у:\n" +
                            chosenSportsman.getName() + " " +
                            chosenSportsman.getSurname() + "\nВид спорта: " +
                            chosenSportsman.getPerformance().getName());

            if (answer == 0) {
                chosenSportsman.getPerformance().getMarks().clear();
            }
            listModelSportsmen.clear();
            readSportsmen();
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        listModelSportsmen = new DefaultListModel<>();
        sportsmenList = new JList<>(listModelSportsmen);
        sportsmenList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        sportsmenLabel = new JLabel("Удаление выступления: ");
        sportsmenLabel.setLocation(100, 10);
        sportsmenLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        removeSportsmanButton = new JButton("Удалить");
        removeSportsmanButton.setLocation(270, 380);
        removeSportsmanButton.setSize(110, 30);

        readSportsmen();

        scrollPaneSportsmen = new JScrollPane(sportsmenList);
        scrollPaneSportsmen.setLocation(50, 50);
        scrollPaneSportsmen.setSize(300, 200);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(removeSportsmanButton);
        panel.add(scrollPaneSportsmen);


        add(panel);
    }

    private void readSportsmen() {

        for (Sportsman sportsman : sportsmen) {
            if (!sportsman.getPerformance().getMarks().isEmpty()) {
                listModelSportsmen.addElement(sportsman.getName() + " " +
                        sportsman.getSurname() + " - " +
                        sportsman.getPerformance().getName());
            }
        }
    }
}
