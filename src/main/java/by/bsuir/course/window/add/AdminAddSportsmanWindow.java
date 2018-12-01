package by.bsuir.course.window.add;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminAddSportsmanWindow extends JFrame {

    private JLabel menuAdminLabel;

    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel ageLabel;
    private JLabel countryLabel;
    private JLabel cityLabel;
    private JLabel sportLabel;

    private JTextField name;
    private JTextField surname;
    private JSpinner age;
    private JTextField country;
    private JTextField city;
    private JComboBox<String> sportsBox;


    private JButton addSportsmanButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;


    public AdminAddSportsmanWindow(JFrame parent, Socket socket,
                                   ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                   List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: меню");
        setSize(400, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        addSportsmanButton.addActionListener(event -> {
            String currentName = name.getText();
            String currentSurname = surname.getText();
            int currentAge = (Integer) age.getValue();
            String currentCountry = country.getText();
            String currentCity = city.getText();
            String currentSport = (String) sportsBox.getSelectedItem();

            if (currentName.isEmpty() || currentSurname.isEmpty()
                    || currentCountry.isEmpty() || currentCity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Необходимо заполнить все поля");
                return;
            }

            for (Sportsman sportsman : sportsmen) {
                if (sportsman.getName().equals(currentName)
                        && sportsman.getSurname().equals(currentSurname)) {
                    JOptionPane.showMessageDialog(this, "Такой спортсмен уже существует");
                    return;
                }
            }

            Sportsman sportsman = new Sportsman();
            sportsman.setName(currentName);
            sportsman.setSurname(currentSurname);
            sportsman.setAge(currentAge);
            Address address = new Address();
            address.setCountry(currentCountry);
            address.setCity(currentCity);
            sportsman.setAddress(address);
            Sport sport;
            switch (currentSport) {
                case "Фигурное катание":
                    sport = new FigureSkating(currentSport);
                    break;
                case "Дайвинг":
                    sport = new Diving(currentSport);
                    break;
                case "Прыжки с трамплина":
                    sport = new SkiJumping(currentSport);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            sportsman.setPerformance(sport);

            sportsmen.add(sportsman);

            JOptionPane.showMessageDialog(this, "Добавление прошло успешно!");
            name.setText("");
            surname.setText("");
            age.setValue(18);
            country.setText("");
            city.setText("");
            sportsBox.setSelectedIndex(0);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        //////////////////////////////////////////////////////////////////
        menuAdminLabel = new JLabel("Добавление спортсмена: ");
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
        ///////////////////////////////////////////////////labels


        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        addSportsmanButton = new JButton("Добавить");
        addSportsmanButton.setLocation(270, 380);
        addSportsmanButton.setSize(110, 30);

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


        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(addSportsmanButton);

        panel.add(menuAdminLabel);
        panel.add(nameLabel);
        panel.add(surnameLabel);
        panel.add(ageLabel);
        panel.add(countryLabel);
        panel.add(cityLabel);
        panel.add(sportLabel);

        panel.add(name);
        panel.add(surname);
        panel.add(age);
        panel.add(country);
        panel.add(city);
        panel.add(sportsBox);

        add(panel);
    }
}
