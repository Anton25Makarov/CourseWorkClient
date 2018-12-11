package by.bsuir.course.window.edit;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminEditSportsmanWindow extends JFrame {

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

    private JLabel sportsmenLabel;
    private JList<String> sportsmenList;
    private JScrollPane scrollPaneSportsmen;
    private DefaultListModel<String> listModelSportsmen;

    private JButton editSportsmanButton;
    private JButton chooseSportsmanButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;

    private Sportsman currentSportsman;


    public AdminEditSportsmanWindow(JFrame parent, Socket socket,
                                    ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                    List<Referee> referees, List<Sportsman> sportsmen) {
        super("Админ: изменение спортсмена");
        setSize(750, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;

        parent.setVisible(false);

        init();

        chooseSportsmanButton.addActionListener(e -> {
            int selectedIndex = sportsmenList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Укажите спортсмена");
                return;
            }

            currentSportsman = sportsmen.get(selectedIndex);

            name.setText(currentSportsman.getName());
            surname.setText(currentSportsman.getSurname());
            age.setValue(currentSportsman.getAge());
            country.setText(currentSportsman.getAddress().getCountry());
            city.setText(currentSportsman.getAddress().getCity());
            sportsBox.setSelectedItem(currentSportsman.getPerformance().getName());
        });

        editSportsmanButton.addActionListener(e -> {
            if (currentSportsman == null) {
                JOptionPane.showMessageDialog(this, "Вы не выбрали спортсмена");
                return;
            }
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
                if (currentSportsman.equals(sportsman)) {
                    continue;
                }
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
            SoloSport soloSport;
            switch (currentSport) {
                case "Фигурное катание":
                    soloSport = new FigureSkating(currentSport);
                    break;
                case "Дайвинг":
                    soloSport = new Diving(currentSport);
                    break;
                case "Прыжки с трамплина":
                    soloSport = new SkiJumping(currentSport);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
            sportsman.setPerformance(soloSport);

            if (!sportsman.getPerformance().getName().equals(currentSportsman.getPerformance().getName())) {
                int answer = JOptionPane.showConfirmDialog(this,
                        "Если вы измените спорт результаты аннулируются:\nРанее указанный спорт: " +
                                currentSportsman.getPerformance().getName() + "\nНовый спорт: " +
                                sportsman.getPerformance().getName());
                if (answer == 0) { // 'yes'
                    currentSportsman.setName(currentName);
                    currentSportsman.setSurname(currentSurname);
                    currentSportsman.setAge(currentAge);
                    currentSportsman.setAddress(address);
                    currentSportsman.setPerformance(soloSport);

                    listModelSportsmen.clear();
                    readSportsmen();
                } else {
                    return;
                }
            } else {
                currentSportsman.setName(currentName);
                currentSportsman.setSurname(currentSurname);
                currentSportsman.setAge(currentAge);
                currentSportsman.setAddress(address);
                currentSportsman.getPerformance().setName(currentSport);

                listModelSportsmen.clear();
                readSportsmen();
            }

            JOptionPane.showMessageDialog(this, "Изменение прошло успешно!");
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
        listModelSportsmen = new DefaultListModel<>();
        sportsmenList = new JList<>(listModelSportsmen);
        sportsmenList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        sportsmenLabel = new JLabel("Изменение спортсмена: ");
        sportsmenLabel.setLocation(100, 10);
        sportsmenLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        chooseSportsmanButton = new JButton("Выбрать");
        chooseSportsmanButton.setLocation(270, 380);
        chooseSportsmanButton.setSize(110, 30);

        readSportsmen();

        scrollPaneSportsmen = new JScrollPane(sportsmenList);
        scrollPaneSportsmen.setLocation(50, 50);
        scrollPaneSportsmen.setSize(300, 200);


        menuAdminLabel = new JLabel("Изменение спортсмена: ");
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

        editSportsmanButton = new JButton("Изменить");
        editSportsmanButton.setLocation(600, 380);
        editSportsmanButton.setSize(110, 30);

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


        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(chooseSportsmanButton);
        panel.add(scrollPaneSportsmen);

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
        panel.add(editSportsmanButton);


        add(panel);
    }

    private void readSportsmen() {
        for (Sportsman sportsman : sportsmen) {
            listModelSportsmen.addElement(sportsman.getName() + " " +
                    sportsman.getSurname() + " - " +
                    sportsman.getPerformance().getName());
        }
    }
}
