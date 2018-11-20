package by.bsuir.course.window;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.User;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class AuthorisationWindow extends JFrame {

    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField login;
    private JPasswordField password;
    private JButton ok;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    public AuthorisationWindow() {
        super("Авторизация");
        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loginLabel = new JLabel("Логин: ");
        loginLabel.setLocation(50, 50);
        loginLabel.setSize(75, 50);

        login = new JTextField();
        login.setLocation(110, 60);
        login.setSize(100, 30);

        passwordLabel = new JLabel("Пароль: ");
        passwordLabel.setLocation(50, 100);
        passwordLabel.setSize(75, 50);

        password = new JPasswordField();
        password.setLocation(110, 110);
        password.setSize(100, 30);

        ok = new JButton("Ok");
        ok.setLocation(90, 160);
        ok.setSize(100, 30);

        panel = new JPanel();
        panel.setLayout(null);
        panel.add(loginLabel);
        panel.add(login);
        panel.add(password);
        panel.add(passwordLabel);
        panel.add(ok);

        add(panel);

        ok.addActionListener(event -> {
            try {
                if (login.getText().isEmpty() || String.copyValueOf(password.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Необходимо ввести логин и пароль");
                    return;
                }
                createSocket();
                if (socket == null) {
                    return;
                }
                objectOutputStream.writeObject("authorisation");

                Referee referee = new Referee();
                referee.setLogin(login.getText());
                referee.setPassword(String.copyValueOf(password.getPassword()));

                objectOutputStream.writeObject(referee);

                String answer = (String) objectInputStream.readObject();
                password.setText("");
                if (!answer.equals("false")) {
                  /*  JOptionPane.showMessageDialog(this,
                            answer);*/
                    if (answer.equals("Admin")) {
                        MenuAdminWindow menuAdminWindow =
                                new MenuAdminWindow(this, socket,
                                        objectOutputStream, objectInputStream);
                        menuAdminWindow.setVisible(true);
                        menuAdminWindow.setLocationRelativeTo(null);
                    } else {

                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Неверно указаны логин или пароль");
                }

            } catch (EOFException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createSocket() {
        try { // установка соединения с сервером
            if (socket == null || objectOutputStream == null || objectInputStream == null) {
                socket = new Socket(InetAddress.getLocalHost(), 8071);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            }
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(this, "Не удалось подключиться к серверу");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
