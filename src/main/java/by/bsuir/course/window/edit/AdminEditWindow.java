package by.bsuir.course.window.edit;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import by.bsuir.course.window.remove.AdminRemovePerformanceWindow;
import by.bsuir.course.window.remove.AdminRemoveRefereeWindow;
import by.bsuir.course.window.remove.AdminRemoveSportsmanWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminEditWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton editSportsmanButton;
    private JButton editRefereeButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminEditWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                           List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: редактрирование");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        editRefereeButton.addActionListener(event -> {
            AdminEditRefereeWindow adminEditRefereeWindow =
                    new AdminEditRefereeWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminEditRefereeWindow.setVisible(true);
            adminEditRefereeWindow.setLocationRelativeTo(null);
        });

        editSportsmanButton.addActionListener(event -> {
            AdminEditSportsmanWindow adminEditSportsmanWindow =
                    new AdminEditSportsmanWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            referees, sportsmen);
            adminEditSportsmanWindow.setVisible(true);
            adminEditSportsmanWindow.setLocationRelativeTo(null);
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

        menuAdminLabel = new JLabel("Редиктирование: ");
        menuAdminLabel.setLocation(100, 10);
        menuAdminLabel.setSize(150, 50);

        editSportsmanButton = new JButton("Изменить спортсмена");
        editSportsmanButton.setLocation(50, 70);
        editSportsmanButton.setSize(180, 30);

        editRefereeButton = new JButton("Изменить судью");
        editRefereeButton.setLocation(50, 120);
        editRefereeButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 180);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(editRefereeButton);
        panel.add(editSportsmanButton);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
