package by.bsuir.course.window.add;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminAddRefereeWindow extends JFrame {

    private JLabel menuAdminLabel;

    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel ageLabel;
    private JLabel countryLabel;
    private JLabel cityLabel;
    private JLabel sportLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;

    private JTextField name;
    private JTextField surname;
    private JSpinner age;
    private JTextField country;
    private JTextField city;
    private JComboBox<String> sportsBox;
    private JTextField login;
    private JTextField password;


    private JButton addRefereeButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminAddRefereeWindow(JFrame parent, Socket socket,
                                 ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                 List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: Добавление рефери");
        setSize(400, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        addRefereeButton.addActionListener(event -> {
            String currentName = name.getText();
            String currentSurname = surname.getText();
            int currentAge = (Integer) age.getValue();
            String currentCountry = country.getText();
            String currentCity = city.getText();
            String currentSport = (String) sportsBox.getSelectedItem();
            String currentLogin = login.getText();
            String currentPassword = password.getText();


            if (currentName.isEmpty() || currentSurname.isEmpty()
                    || currentCountry.isEmpty() || currentCity.isEmpty()
                    || currentLogin.isEmpty() || currentPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Необходимо заполнить все поля");
                return;
            }

            for (Referee referee : referees) {
                if (referee.getLogin().equals(currentLogin)) {
                    JOptionPane.showMessageDialog(this, "Такой рефери уже существует");
                    return;
                }
            }

            Referee referee = new Referee();
            referee.setName(currentName);
            referee.setSurname(currentSurname);
            referee.setAge(currentAge);
            Address address = new Address();
            address.setCountry(currentCountry);
            address.setCity(currentCity);
            referee.setAddress(address);
            referee.setSport(currentSport);
            referee.setLogin(currentLogin);
            referee.setPassword(currentPassword);

            referees.add(referee);

            JOptionPane.showMessageDialog(this, "Добавление прошло успешно!");
            name.setText("");
            surname.setText("");
            age.setValue(18);
            country.setText("");
            city.setText("");
            login.setText("");
            password.setText("");
            sportsBox.setSelectedIndex(0);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        //////////////////////////////////////////////////////////////////
        menuAdminLabel = new JLabel("Добавление рефери: ");
        menuAdminLabel.setLocation(100, 10);
        menuAdminLabel.setSize(200, 50);

        nameLabel = new JLabel("Имя: ");
        nameLabel.setLocation(10, 70);
        nameLabel.setSize(80, 50);

        surnameLabel = new JLabel("Фамилия: ");
        surnameLabel.setLocation(10, 120);
        surnameLabel.setSize(80, 50);

        ageLabel = new JLabel("Полных лет: ");
        ageLabel.setLocation(10, 170);
        ageLabel.setSize(80, 50);

        countryLabel = new JLabel("Страна: ");
        countryLabel.setLocation(10, 220);
        countryLabel.setSize(80, 50);

        cityLabel = new JLabel("Город: ");
        cityLabel.setLocation(10, 270);
        cityLabel.setSize(80, 50);

        sportLabel = new JLabel("Спорт: ");
        sportLabel.setLocation(10, 320);
        sportLabel.setSize(80, 50);

        loginLabel = new JLabel("Логин: ");
        loginLabel.setLocation(10, 370);
        loginLabel.setSize(80, 50);

        passwordLabel = new JLabel("Пароль: ");
        passwordLabel.setLocation(10, 420);
        passwordLabel.setSize(80, 50);
        ///////////////////////////////////////////////////labels


        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 480);
        backButton.setSize(80, 30);

        addRefereeButton = new JButton("Добавить");
        addRefereeButton.setLocation(270, 480);
        addRefereeButton.setSize(110, 30);

        name = new JTextField();
        name.setLocation(100, 80);
        name.setSize(150, 30);

        surname = new JTextField();
        surname.setLocation(100, 130);
        surname.setSize(150, 30);

        SpinnerModel ageModel =
                new SpinnerNumberModel(18, 18, 80, 1);
        age = new JSpinner(ageModel);
        age.setLocation(100, 180);
        age.setSize(80, 30);

        country = new JTextField();
        country.setLocation(100, 230);
        country.setSize(150, 30);

        city = new JTextField();
        city.setLocation(100, 280);
        city.setSize(150, 30);

        String[] items = {
                "Фигурное катание",
                "Дайвинг",
                "Прыжки с трамплина"
        };
        sportsBox = new JComboBox<>(items);
        sportsBox.setLocation(100, 330);
        sportsBox.setSize(150, 30);

        login = new JTextField();
        login.setLocation(100, 380);
        login.setSize(150, 30);

        password = new JTextField();
        password.setLocation(100, 430);
        password.setSize(150, 30);


        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(addRefereeButton);

        panel.add(menuAdminLabel);
        panel.add(nameLabel);
        panel.add(surnameLabel);
        panel.add(ageLabel);
        panel.add(countryLabel);
        panel.add(cityLabel);
        panel.add(sportLabel);
        panel.add(loginLabel);
        panel.add(passwordLabel);


        panel.add(name);
        panel.add(surname);
        panel.add(age);
        panel.add(country);
        panel.add(city);
        panel.add(sportsBox);
        panel.add(login);
        panel.add(password);

        add(panel);
    }
}
