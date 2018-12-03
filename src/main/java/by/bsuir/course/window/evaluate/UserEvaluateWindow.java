package by.bsuir.course.window.evaluate;

import by.bsuir.course.entities.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class UserEvaluateWindow extends JFrame {

    private JLabel sportsmenLabel;

    private JButton evaluateSportsman;

    private JList<String> sportsmenList;

    private Sportsman currentSportsman;

    private JScrollPane scrollPaneSportsmen;

    private DefaultListModel<String> listModelSportsmen;


    private JLabel marksTitleLabel;
    private JLabel technicalMarkLabel;
    private JLabel artistryMarkLabel;
    private JSpinner technicalMark;
    private JSpinner artistryMark;

    private JLabel markLabel;
    private JSpinner markSkiJumping;
    private JSpinner markDiving;

    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private Referee entryReferee;


    public UserEvaluateWindow(JFrame parent, Socket socket,
                              ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                              List<Referee> referees, List<Sportsman> sportsmen,
                              Referee entryReferee) {
        super("Выставление оценок");
        setSize(700, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.referees = referees;
        this.sportsmen = sportsmen;
        this.entryReferee = entryReferee;

        parent.setVisible(false);

        init();


        evaluateSportsman.addActionListener(e -> {
            int selectedIndex = sportsmenList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Выберите спортсмена");
                return;
            }

            String[] sportsmanParts = sportsmenList.getSelectedValue().split(" ");


            for (Sportsman sportsman : sportsmen) {
                if (sportsman.getName().equals(sportsmanParts[0]) && sportsman.getSurname().equals(sportsmanParts[1])) {
                    currentSportsman = sportsman;
                    break;
                }
            }

            Mark mark;
            switch (entryReferee.getSport()) {
                case "Фигурное катание":
                    mark = new FigureSkatingMark((Double) technicalMark.getValue(), (Double) artistryMark.getValue());
                    currentSportsman.getPerformance().addResult(entryReferee, mark);
                    break;
                case "Прыжки с трамплина":
                    mark = new SkiJumpingMark((Double) markSkiJumping.getValue());
                    currentSportsman.getPerformance().addResult(entryReferee, mark);
                    break;
                case "Дайвинг":
                    mark = new DivingMark((Double) markDiving.getValue());
                    currentSportsman.getPerformance().addResult(entryReferee, mark);
                    break;
                default:
                    throw new UnsupportedOperationException("unknown sport: " + entryReferee.getSport());

            }


            technicalMark.setValue(0);
            artistryMark.setValue(0);
            markSkiJumping.setValue(0);
            markDiving.setValue(0);
            JOptionPane.showMessageDialog(this, "Вы успешно оценили спортсмена");
            listModelSportsmen.clear();
            readSportsmen();
        });


        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        panel = new JPanel();
        panel.setLayout(null);
        listModelSportsmen = new DefaultListModel<>();

        sportsmenList = new JList<>(listModelSportsmen);
        sportsmenList.setLayoutOrientation(JList.VERTICAL);


        sportsmenLabel = new JLabel("Спортсмены");
        sportsmenLabel.setLocation(100, 10);
        sportsmenLabel.setSize(100, 50);

        marksTitleLabel = new JLabel("Оцените спортсмена");
        marksTitleLabel.setLocation(500, 50);
        marksTitleLabel.setSize(150, 30);

        technicalMarkLabel = new JLabel("За технику: ");
        technicalMarkLabel.setLocation(400, 110);
        technicalMarkLabel.setSize(110, 30);

        artistryMarkLabel = new JLabel("За артистизм: ");
        artistryMarkLabel.setLocation(400, 160);
        artistryMarkLabel.setSize(110, 30);

//////////////////////////
        markLabel = new JLabel("Оценка: ");
        markLabel.setLocation(400, 110);
        markLabel.setSize(110, 30);

        markDiving = new JSpinner(new SpinnerNumberModel(10, 0, 10, 0.1));
        markDiving.setLocation(520, 110);
        markDiving.setSize(80, 30);

        markSkiJumping = new JSpinner(new SpinnerNumberModel(20, 0, 20, 0.1));
        markSkiJumping.setLocation(520, 110);
        markSkiJumping.setSize(80, 30);


//////////////////////////


        technicalMark = new JSpinner(new SpinnerNumberModel(6, 0, 6, 0.1));
        technicalMark.setLocation(520, 110);
        technicalMark.setSize(80, 30);

        artistryMark = new JSpinner(new SpinnerNumberModel(6, 0, 6, 0.1));
        artistryMark.setLocation(520, 160);
        artistryMark.setSize(80, 30);


        backButton = new JButton("Назад");
        backButton.setLocation(10, 370);
        backButton.setSize(120, 30);

        /////////////////////////////////

        evaluateSportsman = new JButton("Оценить");
        evaluateSportsman.setLocation(400, 370);
        evaluateSportsman.setSize(120, 30);

        readSportsmen();

        scrollPaneSportsmen = new JScrollPane(sportsmenList);
        scrollPaneSportsmen.setLocation(50, 50);
        scrollPaneSportsmen.setSize(300, 200);


        panel.add(sportsmenLabel);
        panel.add(scrollPaneSportsmen);
        panel.add(backButton);
        panel.add(evaluateSportsman);
        panel.add(marksTitleLabel);
        if (entryReferee.getSport().equals("Фигурное катание")) {
            panel.add(technicalMark);
            panel.add(artistryMark);
            panel.add(artistryMarkLabel);
            panel.add(technicalMarkLabel);
        }
        if (entryReferee.getSport().equals("Дайвинг") || entryReferee.getSport().equals("Прыжки с трамплина")) {
            panel.add(markLabel);
        }
        if (entryReferee.getSport().equals("Дайвинг")) {
            panel.add(markDiving);
        }
        if (entryReferee.getSport().equals("Прыжки с трамплина")) {
            panel.add(markSkiJumping);
        }
        panel.add(markSkiJumping);

        add(panel);
    }

    private void readSportsmen() {
        for (Sportsman sportsman : sportsmen) {
            if (!sportsman.getPerformance().getMarks().isEmpty() &&
                    sportsman.getPerformance().getMarks().containsKey(entryReferee) &&
                    sportsman.getPerformance().getMarks().get(entryReferee) == null) {
                listModelSportsmen.addElement(sportsman.getName() + " " +
                        sportsman.getSurname() + " - " +
                        sportsman.getPerformance().getName());
            }
        }
//        switch (entryReferee.getSport()) {
//            case "Фигурное катание":
//                for (Sportsman sportsman : sportsmen) {
//                    if (!sportsman.getPerformance().getMarks().isEmpty() &&
//                            sportsman.getPerformance().getMarks().containsKey(entryReferee) &&
//                            sportsman.getPerformance().getMarks().get(entryReferee) == null) {
//                        listModelSportsmen.addElement(sportsman.getName() + " " +
//                                sportsman.getSurname() + " - " +
//                                sportsman.getPerformance().getName());
//                    }
//                }
//                break;
//            case "Прыжки с трамплина":
//                for (Sportsman sportsman : sportsmen) {
//                    if (!sportsman.getPerformance().getMarks().isEmpty() &&
//                            sportsman.getPerformance().getMarks().containsKey(entryReferee) &&
//                            sportsman.getPerformance().getMarks().get(entryReferee) == null) {
//                        listModelSportsmen.addElement(sportsman.getName() + " " +
//                                sportsman.getSurname() + " - " +
//                                sportsman.getPerformance().getName());
//                    }
//                }
//                break;
//            case "Дайвинг":
//                for (Sportsman sportsman : sportsmen) {
//                    if (!sportsman.getPerformance().getMarks().isEmpty() &&
//                            sportsman.getPerformance().getMarks().containsKey(entryReferee) &&
//                            sportsman.getPerformance().getMarks().get(entryReferee) == null) {
//                        listModelSportsmen.addElement(sportsman.getName() + " " +
//                                sportsman.getSurname() + " - " +
//                                sportsman.getPerformance().getName());
//                    }
//                }
//                break;
//            default:
//                throw new UnsupportedOperationException("unknown sport: " + entryReferee.getSport());
//
//        }
    }
}
