package by.bsuir.course.window.report;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import by.bsuir.course.window.results.UserShowPerformancesResultsWindow;
import by.bsuir.course.window.show.AdminShowWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class UserReportsWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton performancesShouldEvaluateButton;
    private JButton performancesAlreadyEvaluateButton;
    private JButton showResultsButton;
    private JButton showButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private String currentRefereeLogin;
    private Referee entryReferee;


    public UserReportsWindow(JFrame parent, Socket socket,
                             ObjectOutputStream objectOutputStream,
                             ObjectInputStream objectInputStream,
                             List<Referee> referees, List<Sportsman> sportsmen,
                             Referee entryReferee) {
        super("Рефери: меню");
        setSize(300, 380);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.entryReferee = entryReferee;

        parent.setVisible(false);

        init();

        performancesShouldEvaluateButton.addActionListener(event -> {
            UserReportsShouldEvaluatePerformancesWindow userReportsShouldEvaluatePerformancesWindow =
                    new UserReportsShouldEvaluatePerformancesWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userReportsShouldEvaluatePerformancesWindow.setVisible(true);
            userReportsShouldEvaluatePerformancesWindow.setLocationRelativeTo(null);
        });

        performancesAlreadyEvaluateButton.addActionListener(event -> {
            UserReportsAlreadyEvaluatePerformancesWindow userReportsAlreadyEvaluatePerformancesWindow =
                    new UserReportsAlreadyEvaluatePerformancesWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userReportsAlreadyEvaluatePerformancesWindow.setVisible(true);
            userReportsAlreadyEvaluatePerformancesWindow.setLocationRelativeTo(null);
        });

        showResultsButton.addActionListener(event -> {
            UserShowPerformancesResultsWindow userShowPerformancesResultsWindow =
                    new UserShowPerformancesResultsWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userShowPerformancesResultsWindow.setVisible(true);
            userShowPerformancesResultsWindow.setLocationRelativeTo(null);
        });

        showButton.addActionListener(event -> {
            AdminShowWindow adminShowWindow =
                    new AdminShowWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminShowWindow.setVisible(true);
            adminShowWindow.setLocationRelativeTo(null);
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

        menuAdminLabel = new JLabel("Меню: ");
        menuAdminLabel.setLocation(120, 10);
        menuAdminLabel.setSize(75, 50);

        performancesShouldEvaluateButton = new JButton("Выступлениям, которые необходимо оценить");
        performancesShouldEvaluateButton.setLocation(50, 70);
        performancesShouldEvaluateButton.setSize(180, 30);

        performancesAlreadyEvaluateButton = new JButton("Выступления, которые оценил");
        performancesAlreadyEvaluateButton.setLocation(50, 120);
        performancesAlreadyEvaluateButton.setSize(180, 30);

        showResultsButton = new JButton("Посмотреть результаты");
        showResultsButton.setLocation(50, 170);
        showResultsButton.setSize(180, 30);

        showButton = new JButton("Посмотреть");
        showButton.setLocation(50, 220);
        showButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 270);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(showResultsButton);
        panel.add(backButton);
        panel.add(performancesAlreadyEvaluateButton);
        panel.add(performancesShouldEvaluateButton);
        panel.add(menuAdminLabel);
        panel.add(showButton);

        add(panel);
    }
}
