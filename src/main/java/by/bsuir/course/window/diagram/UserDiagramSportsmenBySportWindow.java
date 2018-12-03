package by.bsuir.course.window.diagram;

import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDiagramSportsmenBySportWindow extends JFrame {

    private JButton backButton;
    private JPanel panel;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private Referee entryReferee;


    public UserDiagramSportsmenBySportWindow(JFrame parent, Socket socket,
                                             ObjectOutputStream objectOutputStream,
                                             ObjectInputStream objectInputStream,
                                             List<Referee> referees, List<Sportsman> sportsmen,
                                             Referee entryReferee) {
        super("Рефери: меню");
        setSize(500, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.sportsmen = sportsmen;
        this.referees = referees;
        this.entryReferee = entryReferee;

        parent.setVisible(false);

        init();


        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }


    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Сохранить");
        menuBar.add(menu);

        JFreeChart chart = ChartFactory.createPieChart("Соотношение спортсменов по спорту",
                createDataset(), true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        getContentPane().add(chartPanel);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 420);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);


        chartPanel.setLocation(50, 50);
        chartPanel.setSize(350, 350);
        panel.add(chartPanel);

//        panel.add(jfxPanel);

        add(panel);
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();


        Set<String> sports = new HashSet<>();

        for (Sportsman sportsman : sportsmen) {
            sports.add(sportsman.getPerformance().getName());
            System.out.print(sportsman.getPerformance().getName() + ", ");
        }

        List<Integer> counts = new ArrayList<>();
        List<String> countriesList = new ArrayList<>(sports);

        for (String string : countriesList) {
            int total = 0;
            for (Sportsman sportsman : sportsmen) {
                if (string.equals(sportsman.getPerformance().getName())) {
                    total++;
                }
            }
            counts.add(total);
        }
        System.out.println();


        for (int i = 0; i < counts.size(); i++) {
            dataset.setValue(countriesList.get(i), new Double(counts.get(i)));
        }

        System.out.println("uniq: " + sports);
        System.out.println("counts: " + counts);

        return dataset;
    }
}
