// Main.java
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("영화 예매 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        DatabaseManager dbManager = new DatabaseManager("jdbc:oracle:thin:@localhost:1521:xe", "movie_user", "password");

        MovieChartPanel movieChartPanel = new MovieChartPanel(mainPanel, cardLayout, dbManager);
        MovieDetailPanel movieDetailPanel = new MovieDetailPanel(mainPanel, cardLayout, dbManager);
        SeatSelectionPanel seatSelectionPanel = new SeatSelectionPanel(mainPanel, cardLayout, dbManager);

        mainPanel.add(movieChartPanel, "MovieChart");
        mainPanel.add(movieDetailPanel, "MovieDetail");
        mainPanel.add(seatSelectionPanel, "SeatSelection");

        cardLayout.show(mainPanel, "MovieChart");

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
