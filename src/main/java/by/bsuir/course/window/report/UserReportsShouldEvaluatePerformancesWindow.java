package by.bsuir.course.window.report;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UserReportsShouldEvaluatePerformancesWindow extends JFrame {
    private static final String FILE_NAME_PATTERN = "[ a-zA-Z\\d]+";
    private JLabel menuAdminLabel;

    private JTextField fileNameField;
    private JButton backButton;
    private JButton createReportButton;
    private JPanel panel;
    private String report;


    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private String currentRefereeLogin;
    private Referee entryReferee;


    public UserReportsShouldEvaluatePerformancesWindow(JFrame parent, Socket socket,
                                                       ObjectOutputStream objectOutputStream,
                                                       ObjectInputStream objectInputStream,
                                                       List<Referee> referees, List<Sportsman> sportsmen,
                                                       Referee entryReferee) {
        super("Рефери: отчёт");
        setSize(320, 380);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.entryReferee = entryReferee;
        this.sportsmen = sportsmen;
        this.referees = referees;

        parent.setVisible(false);

        init();

        createReportButton.addActionListener(e -> {
            String fileName = fileNameField.getText();

            if (fileName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Введите название файла");
                return;
            }

            if (!isValid(fileName)) {
                JOptionPane.showMessageDialog(this,
                        "В названии фала должны быть только цифры и латинские буквы");
                return;
            }

            fileName = entryReferee.getLogin() + " - " + fileName;

            if (isExist(fileName)) {
                JOptionPane.showMessageDialog(this,
                        "Файл с таким названием уже существует");
                return;
            }

            report = createReport();

            try {
                if (!Files.exists(Paths.get("C:\\Users\\Acer\\Desktop\\referees" +
                        entryReferee.getLogin()))) {
                    Files.createDirectory(Paths.get("C:\\Users\\Acer\\Desktop\\referees" +
                            entryReferee.getLogin()));
                }
                Files.write(Paths.get("C:\\Users\\Acer\\Desktop\\referees" +
                        entryReferee.getLogin() + "/" + fileName + ".txt"), report.getBytes());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this,
                        "Не удалось создать отчёт");
                return;
            }

            File file = new File("C:\\Users\\Acer\\Desktop\\referees" +
                    entryReferee.getLogin() + "/" + fileName + ".txt");

            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            fileNameField.setText("");

        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

    }

    private boolean isValid(String fileName) {
        return fileName.matches(FILE_NAME_PATTERN);
    }

    private boolean isExist(String fileName) {
        return Files.exists(Paths.get("C:\\Users\\Acer\\Desktop\\referees" +
                entryReferee.getLogin() + "/" +
                fileName + ".txt"));
    }

    private String createReport() {
        StringBuilder textReport = new StringBuilder("\t\t\t" + entryReferee.getLogin() + "\r\n");
        textReport.append("\t\t").append(entryReferee.getName()).append(" ").append(entryReferee.getSurname());
        textReport.append(" (").append(entryReferee.getSport()).append(")\r\n");
        textReport.append("\tВам необходимо оценить следующих спотсменов:\r\n");
        int counter = 1;
        for (Sportsman sportsman : sportsmen) {

            if (!sportsman.getPerformance().getMarks().isEmpty() &&
                    sportsman.getPerformance().getMarks().containsKey(entryReferee) &&
                    sportsman.getPerformance().getMarks().get(entryReferee) == null) {

                textReport.append(counter).
                        append(") ").
                        append(sportsman.getName()).
                        append(" ").
                        append(sportsman.getSurname()).
                        append(".\r\n");
                counter++;
            }
        }
        if (counter == 1) {
            textReport.append("Пока вы не назначены ни на одно выступление.");
        }
        return textReport.toString();
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Сохранить");
        menuBar.add(menu);


        menuAdminLabel = new JLabel("Укажите название файла: ");
        menuAdminLabel.setLocation(80, 10);
        menuAdminLabel.setSize(160, 50);

        fileNameField = new JTextField();
        fileNameField.setLocation(80, 70);
        fileNameField.setSize(150, 30);

        createReportButton = new JButton("Создать отчёт");
        createReportButton.setLocation(170, 270);
        createReportButton.setSize(120, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 270);
        backButton.setSize(80, 30);


        panel = new JPanel();
        panel.setLayout(null);

        panel.add(fileNameField);
        panel.add(createReportButton);
        panel.add(backButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
