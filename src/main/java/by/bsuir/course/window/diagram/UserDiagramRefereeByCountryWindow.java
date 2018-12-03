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

public class UserDiagramRefereeByCountryWindow extends JFrame {

    private JButton backButton;
    private JPanel panel;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Referee> referees;
    private List<Sportsman> sportsmen;
    private Referee entryReferee;


    public UserDiagramRefereeByCountryWindow(JFrame parent, Socket socket,
                                             ObjectOutputStream objectOutputStream,
                                             ObjectInputStream objectInputStream,
                                             List<Referee> referees, List<Sportsman> sportsmen,
                                             Referee entryReferee) {
        super("Даиграмма");
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

        JFreeChart chart = ChartFactory.createPieChart("Соотношение рефери из рызных стран",
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


        Set<String> countries = new HashSet<>();

        for (Referee referee : referees) {
            countries.add(referee.getAddress().getCountry());
            System.out.print(referee.getAddress().getCountry() + ", ");
        }

        List<Integer> counts = new ArrayList<>();
        List<String> countriesList = new ArrayList<>(countries);

        for (String string : countriesList) {
            int total = 0;
            for (Referee referee : referees) {
                if (string.equals(referee.getAddress().getCountry())) {
                    total++;
                }
            }
            counts.add(total);
        }
        System.out.println();


        for (int i = 0; i < counts.size(); i++) {
            dataset.setValue(countriesList.get(i), new Double(counts.get(i)));
        }

        System.out.println("uniq: " + countries);
        System.out.println("counts: " + counts);

        /*dataset.setValue("IPhone 5s", new Double(20));
        dataset.setValue("SamSung Grand", new Double(20));
        dataset.setValue("MotoG", new Double(40));
        dataset.setValue("Nokia Lumia", new Double(10));*/
        return dataset;
    }
}
