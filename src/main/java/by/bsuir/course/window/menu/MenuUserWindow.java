package by.bsuir.course.window.menu;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import by.bsuir.course.window.diagram.UserDiagramsWindow;
import by.bsuir.course.window.evaluate.UserEvaluateWindow;
import by.bsuir.course.window.report.UserReportsWindow;
import by.bsuir.course.window.results.UserShowPerformancesResultsWindow;
import by.bsuir.course.window.show.AdminShowWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MenuUserWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton reportsButton;
    private JButton showResultsButton;
    private JButton estimateButton;
    private JButton showButton;
    private JButton diagramButton;
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


    public MenuUserWindow(JFrame parent, Socket socket,
                          ObjectOutputStream objectOutputStream,
                          ObjectInputStream objectInputStream,
                          String login) {
        super("Рефери: меню");
        setSize(300, 430);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.currentRefereeLogin = login;

        parent.setVisible(false);

        init();

        reportsButton.addActionListener(event -> {
            UserReportsWindow adminAddWindow =
                    new UserReportsWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            adminAddWindow.setVisible(true);
            adminAddWindow.setLocationRelativeTo(null);
        });

        showResultsButton.addActionListener(event -> {
            UserShowPerformancesResultsWindow userShowPerformancesResultsWindow =
                    new UserShowPerformancesResultsWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userShowPerformancesResultsWindow.setVisible(true);
            userShowPerformancesResultsWindow.setLocationRelativeTo(null);
        });

        estimateButton.addActionListener(event -> {
            UserEvaluateWindow userEvaluateWindow =
                    new UserEvaluateWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userEvaluateWindow.setVisible(true);
            userEvaluateWindow.setLocationRelativeTo(null);
        });

        showButton.addActionListener(event -> {
            AdminShowWindow adminShowWindow =
                    new AdminShowWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminShowWindow.setVisible(true);
            adminShowWindow.setLocationRelativeTo(null);
        });

        diagramButton.addActionListener(event -> {
            UserDiagramsWindow userDiagramsWindow =
                    new UserDiagramsWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen, entryReferee);
            userDiagramsWindow.setVisible(true);
            userDiagramsWindow.setLocationRelativeTo(null);
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

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        loadAllFromServer();
    }

    private void loadAllFromServer() {
        try {
            objectOutputStream.writeObject("getAll");

            objectOutputStream.writeObject(null);

            referees = (List<Referee>) objectInputStream.readObject();

            sportsmen = (List<Sportsman>) objectInputStream.readObject();

            setCurrentReferee();

//            System.out.println(referees);
//            System.out.println(sportsmen);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentReferee() {
        for (Referee referee : referees) {
            if (currentRefereeLogin.equals(referee.getLogin())) {
                entryReferee = referee;
                break;
            }
        }
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

        reportsButton = new JButton("Отчёт");
        reportsButton.setLocation(50, 70);
        reportsButton.setSize(180, 30);

        showResultsButton = new JButton("Результаты выступлений");
        showResultsButton.setLocation(45, 120);
        showResultsButton.setSize(190, 30);

        estimateButton = new JButton("Выставить оценки");
        estimateButton.setLocation(50, 170);
        estimateButton.setSize(180, 30);

        showButton = new JButton("Посмотреть");
        showButton.setLocation(50, 220);
        showButton.setSize(180, 30);

        diagramButton = new JButton("Аналитическая информация");
        diagramButton.setLocation(35, 270);
        diagramButton.setSize(210, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 320);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(estimateButton);
        panel.add(backButton);
        panel.add(showResultsButton);
        panel.add(reportsButton);
        panel.add(menuAdminLabel);
        panel.add(showButton);
        panel.add(diagramButton);

        add(panel);
    }
}
