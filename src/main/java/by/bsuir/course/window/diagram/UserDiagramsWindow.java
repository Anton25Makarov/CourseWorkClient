package by.bsuir.course.window.diagram;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class UserDiagramsWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton byCountryButton;
    private JButton byCountryRefereeButton;
    private JButton bySportSportsmenButton;
    private JButton bySportRefereesButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private Referee entryReferee;


    public UserDiagramsWindow(JFrame parent, Socket socket,
                              ObjectOutputStream objectOutputStream,
                              ObjectInputStream objectInputStream,
                              List<Referee> referees, List<Sportsman> sportsmen,
                              Referee entryReferee) {
        super("Диаграммы");
        setSize(300, 380);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.sportsmen = sportsmen;
        this.referees = referees;
        this.entryReferee = entryReferee;

        parent.setVisible(false);

        init();

        byCountryButton.addActionListener(event -> {
            UserDiagramSportsmenByCountryWindow adminAddWindow =
                    new UserDiagramSportsmenByCountryWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            adminAddWindow.setVisible(true);
            adminAddWindow.setLocationRelativeTo(null);
        });

        byCountryRefereeButton.addActionListener(event -> {
            UserDiagramRefereeByCountryWindow userDiagramRefereeByCountryWindow =
                    new UserDiagramRefereeByCountryWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userDiagramRefereeByCountryWindow.setVisible(true);
            userDiagramRefereeByCountryWindow.setLocationRelativeTo(null);
        });

        bySportSportsmenButton.addActionListener(event -> {
            UserDiagramSportsmenBySportWindow userDiagramSportsmenBySportWindow =
                    new UserDiagramSportsmenBySportWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userDiagramSportsmenBySportWindow.setVisible(true);
            userDiagramSportsmenBySportWindow.setLocationRelativeTo(null);
        });

        bySportRefereesButton.addActionListener(event -> {
            UserDiagramRefereeBySportWindow userDiagramRefereeBySportWindow =
                    new UserDiagramRefereeBySportWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userDiagramRefereeBySportWindow.setVisible(true);
            userDiagramRefereeBySportWindow.setLocationRelativeTo(null);
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

        menuAdminLabel = new JLabel("Диаграммы: ");
        menuAdminLabel.setLocation(107, 10);
        menuAdminLabel.setSize(75, 50);

        byCountryButton = new JButton("Спортсмены по странам");
        byCountryButton.setLocation(50, 70);
        byCountryButton.setSize(180, 30);

        byCountryRefereeButton = new JButton("Рефери по странам");
        byCountryRefereeButton.setLocation(50, 120);
        byCountryRefereeButton.setSize(180, 30);

        bySportSportsmenButton = new JButton("Спортсмены по спорту");
        bySportSportsmenButton.setLocation(50, 170);
        bySportSportsmenButton.setSize(180, 30);

        bySportRefereesButton = new JButton("Судьи по спорту");
        bySportRefereesButton.setLocation(50, 220);
        bySportRefereesButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 270);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(bySportSportsmenButton);
        panel.add(backButton);
        panel.add(byCountryRefereeButton);
        panel.add(byCountryButton);
        panel.add(menuAdminLabel);
        panel.add(bySportRefereesButton);

        add(panel);
    }
}
