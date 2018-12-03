package by.bsuir.course.window.remove;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminRemoveRefereeWindow extends JFrame {

    private JLabel refereeLabel;
    private JList<String> refereeList;
    private JScrollPane scrollPaneReferee;
    private DefaultListModel<String> listModelReferee;

    private JButton removeRefereeButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminRemoveRefereeWindow(JFrame parent, Socket socket,
                                    ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                    List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: удаление рефери");
        setSize(400, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        removeRefereeButton.addActionListener(e -> {
            int selectedIndex = refereeList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Укажите рефери");
                return;
            }

            Referee chosenReferee = referees.get(selectedIndex);

            int answer = JOptionPane.showConfirmDialog(this,
                    "Вы действительно хотите удалить:\n" +
                    chosenReferee.getName() + " " +
                    chosenReferee.getSurname() + "\nВид спорта: " +
                    chosenReferee.getSport() + "\nLogin: " +
                    chosenReferee.getLogin());

            if (answer == 0) {
                referees.remove(chosenReferee);
            }
            listModelReferee.clear();
            readReferee();
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        listModelReferee = new DefaultListModel<>();
        refereeList = new JList<>(listModelReferee);
        refereeList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        refereeLabel = new JLabel("Удаление рефери: ");
        refereeLabel.setLocation(100, 10);
        refereeLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        removeRefereeButton = new JButton("Удалить");
        removeRefereeButton.setLocation(270, 380);
        removeRefereeButton.setSize(110, 30);

        readReferee();

        scrollPaneReferee = new JScrollPane(refereeList);
        scrollPaneReferee.setLocation(50, 50);
        scrollPaneReferee.setSize(300, 200);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(removeRefereeButton);
        panel.add(scrollPaneReferee);


        add(panel);
    }

    private void readReferee() {

        for (Referee referee : referees) {
            listModelReferee.addElement(referee.getName() + " " +
                    referee.getSurname() + " - " +
                    referee.getLogin());
        }
    }
}
