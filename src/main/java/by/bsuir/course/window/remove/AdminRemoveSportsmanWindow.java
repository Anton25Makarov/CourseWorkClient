package by.bsuir.course.window.remove;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminRemoveSportsmanWindow extends JFrame {

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


    public AdminRemoveSportsmanWindow(JFrame parent, Socket socket,
                                      ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                      List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: удаление спортсмена");
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

            Sportsman chosenSportsman = sportsmen.get(selectedIndex);
            int answer = JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить:\n" +
                    chosenSportsman.getName() + " " +
                    chosenSportsman.getSurname() + "\nВид спорта: " +
                    chosenSportsman.getPerformance().getName());

            if (answer == 0) {
                sportsmen.remove(chosenSportsman);
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
        sportsmenLabel = new JLabel("Удаление спортсмена: ");
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
            listModelSportsmen.addElement(sportsman.getName() + " " +
                    sportsman.getSurname() + " - " +
                    sportsman.getPerformance().getName());
        }
    }
}
