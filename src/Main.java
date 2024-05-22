import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("영화 예매 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        MovieChartPanel movieChartPanel = new MovieChartPanel(mainPanel, cardLayout);
        MovieDetailPanel movieDetailPanel = new MovieDetailPanel(mainPanel, cardLayout);
        ReservationPanel reservationPanel = new ReservationPanel(mainPanel, cardLayout);
        SeatSelectionPanel seatSelectionPanel = new SeatSelectionPanel(mainPanel, cardLayout);

        mainPanel.add(movieChartPanel, "MovieChart");

        mainPanel.add(movieDetailPanel, "MovieDetail");
        mainPanel.add(reservationPanel, "Reservation");
        mainPanel.add(seatSelectionPanel, "SeatSelection");

        frame.add(mainPanel);
        cardLayout.show(mainPanel, "MovieChart");

        frame.setVisible(true);
    }
}
