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
        super("Отчёты");
        setSize(370, 270);
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

        menuAdminLabel = new JLabel("Создание отчёта: ");
        menuAdminLabel.setLocation(120, 10);
        menuAdminLabel.setSize(150, 50);

        performancesShouldEvaluateButton = new JButton("Выступления, которые необходимо оценить");
        performancesShouldEvaluateButton.setLocation(10, 70);
        performancesShouldEvaluateButton.setSize(330, 30);

        performancesAlreadyEvaluateButton = new JButton("Выступления, которые оценил");
        performancesAlreadyEvaluateButton.setLocation(10, 120);
        performancesAlreadyEvaluateButton.setSize(330, 30);


        backButton = new JButton("Назад");
        backButton.setLocation(10, 170);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(performancesAlreadyEvaluateButton);
        panel.add(performancesShouldEvaluateButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
