package by.bsuir.course.window.edit;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Ref;
import java.util.List;

public class AdminEditRefereeWindow extends JFrame {

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
    private JPasswordField password;

    private JLabel refereeLabel;
    private JList<String> refereeList;
    private JScrollPane scrollPaneReferee;
    private DefaultListModel<String> listModelReferee;

    private JButton editRefereeButton;
    private JButton chooseRefereeButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;

    private Referee currentReferee;


    public AdminEditRefereeWindow(JFrame parent, Socket socket,
                                  ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                  List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: меню");
        setSize(750, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        chooseRefereeButton.addActionListener(e -> {
            int selectedIndex = refereeList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Укажите рефери");
                return;
            }

            currentReferee = referees.get(selectedIndex);

            name.setText(currentReferee.getName());
            surname.setText(currentReferee.getSurname());
            age.setValue(currentReferee.getAge());
            country.setText(currentReferee.getAddress().getCountry());
            city.setText(currentReferee.getAddress().getCity());
            sportsBox.setSelectedItem(currentReferee.getSport());
            login.setText(currentReferee.getLogin());
            password.setText(currentReferee.getPassword());
        });

        editRefereeButton.addActionListener(e -> {
            if (currentReferee == null) {
                JOptionPane.showMessageDialog(this, "Вы не выбрали рефери");
                return;
            }
            String currentName = name.getText();
            String currentSurname = surname.getText();
            int currentAge = (Integer) age.getValue();
            String currentCountry = country.getText();
            String currentCity = city.getText();
            String currentSport = (String) sportsBox.getSelectedItem();
            String currentLogin = login.getText();
            String currentPassword = String.valueOf(password.getPassword());

            if (currentName.isEmpty() || currentSurname.isEmpty()
                    || currentCountry.isEmpty() || currentCity.isEmpty()
                    || currentLogin.isEmpty() || currentPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Необходимо заполнить все поля");
                return;
            }

            for (Referee referee : referees) {
                if (currentReferee.equals(referee)) {
                    continue;
                }
                if ((referee.getName().equals(currentName)
                        && referee.getSurname().equals(currentSurname))
                        || referee.getLogin().equals(currentLogin)) {
                    JOptionPane.showMessageDialog(this, "Такой рефери уже существует");
                    return;
                }
            }

            Address address = new Address();
            address.setCountry(currentCountry);
            address.setCity(currentCity);
            currentReferee.setName(currentName);
            currentReferee.setSurname(currentSurname);
            currentReferee.setAge(currentAge);
            currentReferee.setAddress(address);
            currentReferee.setSport(currentSport);
            currentReferee.setLogin(currentLogin);
            currentReferee.setPassword(currentPassword);

            listModelReferee.clear();
            readReferee();

            JOptionPane.showMessageDialog(this, "Изменение прошло успешно!");
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
        listModelReferee = new DefaultListModel<>();
        refereeList = new JList<>(listModelReferee);
        refereeList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        refereeLabel = new JLabel("Изменение рефери: ");
        refereeLabel.setLocation(100, 10);
        refereeLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 480);
        backButton.setSize(80, 30);

        chooseRefereeButton = new JButton("Выбрать");
        chooseRefereeButton.setLocation(270, 480);
        chooseRefereeButton.setSize(110, 30);

        readReferee();

        scrollPaneReferee = new JScrollPane(refereeList);
        scrollPaneReferee.setLocation(50, 50);
        scrollPaneReferee.setSize(300, 200);


        menuAdminLabel = new JLabel("Изменение рефери: ");
        menuAdminLabel.setLocation(500, 10);
        menuAdminLabel.setSize(200, 50);

        nameLabel = new JLabel("Имя: ");
        nameLabel.setLocation(400, 70);
        nameLabel.setSize(80, 50);

        surnameLabel = new JLabel("Фамилия: ");
        surnameLabel.setLocation(400, 120);
        surnameLabel.setSize(80, 50);

        ageLabel = new JLabel("Полных лет: ");
        ageLabel.setLocation(400, 170);
        ageLabel.setSize(80, 50);

        countryLabel = new JLabel("Страна: ");
        countryLabel.setLocation(400, 220);
        countryLabel.setSize(80, 50);

        cityLabel = new JLabel("Город: ");
        cityLabel.setLocation(400, 270);
        cityLabel.setSize(80, 50);

        sportLabel = new JLabel("Спорт: ");
        sportLabel.setLocation(400, 320);
        sportLabel.setSize(80, 50);

        loginLabel = new JLabel("Логин: ");
        loginLabel.setLocation(400, 370);
        loginLabel.setSize(80, 50);

        passwordLabel = new JLabel("Пароль: ");
        passwordLabel.setLocation(400, 420);
        passwordLabel.setSize(80, 50);

        editRefereeButton = new JButton("Изменить");
        editRefereeButton.setLocation(600, 480);
        editRefereeButton.setSize(110, 30);

        name = new JTextField();
        name.setLocation(490, 80);
        name.setSize(150, 30);

        surname = new JTextField();
        surname.setLocation(490, 130);
        surname.setSize(150, 30);

        SpinnerModel ageModel =
                new SpinnerNumberModel(18, 18, 80, 1);
        age = new JSpinner(ageModel);
        age.setLocation(490, 180);
        age.setSize(80, 30);

        country = new JTextField();
        country.setLocation(490, 230);
        country.setSize(150, 30);

        city = new JTextField();
        city.setLocation(490, 280);
        city.setSize(150, 30);

        String[] items = {
                "Фигурное катание",
                "Дайвинг",
                "Прыжки с трамплина"
        };
        sportsBox = new JComboBox<>(items);
        sportsBox.setLocation(490, 330);
        sportsBox.setSize(150, 30);

        login = new JTextField();
        login.setLocation(490, 380);
        login.setSize(150, 30);

        password = new JPasswordField();
        password.setLocation(490, 430);
        password.setSize(150, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(chooseRefereeButton);
        panel.add(scrollPaneReferee);

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
        panel.add(editRefereeButton);
        panel.add(login);
        panel.add(password);


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
