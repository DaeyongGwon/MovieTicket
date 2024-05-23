import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class SeatSelectionPanel extends JPanel {
    private Movie movie;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JButton[][] seats = new JButton[5][5];
    private DatabaseManager dbManager;
    private Showtime selectedShowtime;

    public SeatSelectionPanel(JPanel mainPanel, CardLayout cardLayout, DatabaseManager dbManager) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.dbManager = dbManager;
        setLayout(new BorderLayout());
    }

    public void setMovie(Movie movie, String theater, String date, String time) {
        this.movie = movie;
        removeAll();

        // Left: Display movie poster
        ImageIcon posterIcon = new ImageIcon(movie.getPosterPath());
        Image posterImage = posterIcon.getImage();
        int width = posterImage.getWidth(null);
        int height = posterImage.getHeight(null);
        double aspectRatio = (double) width / height;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxPosterWidth = (int) (0.8 * screenSize.width / 2 * 0.8);
        int maxPosterHeight = (int) (0.8 * maxPosterWidth / aspectRatio);

        Image scaledPosterImage = posterImage.getScaledInstance(maxPosterWidth, maxPosterHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPosterIcon = new ImageIcon(scaledPosterImage);
        JLabel posterLabel = new JLabel(scaledPosterIcon);
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.add(posterLabel, BorderLayout.CENTER);
        posterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(posterPanel, BorderLayout.WEST);

        // Right: Display show information
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(titleLabel);

        JLabel theaterLabel = new JLabel("Theater: " + theater);
        infoPanel.add(theaterLabel);

        JLabel dateLabel = new JLabel("Date: " + date);
        infoPanel.add(dateLabel);

        JLabel timeLabel = new JLabel("Time: " + time);
        infoPanel.add(timeLabel);

        add(infoPanel, BorderLayout.NORTH);

        // Right: Display seat selection screen
        JPanel seatPanel = new JPanel(new GridLayout(5, 5, 5, 5));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                seats[i][j] = new JButton();
                seats[i][j].setBackground(Color.RED);
                seats[i][j].setPreferredSize(new Dimension(20, 20));
                seats[i][j].addActionListener(new SeatSelectionListener(i, j));
                seatPanel.add(seats[i][j]);
            }
        }

        add(seatPanel, BorderLayout.CENTER);

        // Add 'Reserve' button
        JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserveSelectedSeats();
            }
        });
        add(reserveButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private class SeatSelectionListener implements ActionListener {
        private int row, col;

        public SeatSelectionListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton selectedSeat = seats[row][col];
            if (selectedSeat.getBackground() == Color.RED) {
                selectedSeat.setBackground(Color.GRAY);
            } else {
                selectedSeat.setBackground(Color.RED);
            }
        }
    }

    private void reserveSelectedSeats() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton seat = seats[i][j];
                if (seat.getBackground() == Color.GRAY) {
                    reserveSeat(i, j);
                }
            }
        }
    }

    private void reserveSeat(int row, int col) {
        String query = "INSERT INTO Reservations (reservation_id, showtime_id, seat_id, reserved_date, reserved_time) VALUES (RESERVATION_ID_SEQ.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, selectedShowtime.getId());
            pstmt.setInt(2, getSeatId(row, col));
            pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setTime(4, java.sql.Time.valueOf(LocalTime.now()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getSeatId(int row, int col) {
        return 0; // Placeholder
    }

    public static class Showtime {
        private int id;
        private int movieId;
        private int theaterId;
        private LocalDate date;
        private LocalTime time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public void setTheaterId(int theaterId) {
            this.theaterId = theaterId;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }


        // Constructor, getters and setters
    }

    public Showtime getSelectedShowtime(String movieTitle, LocalDate showDate, LocalTime showTime) throws SQLException {
        Showtime selectedShowtime = null;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            String query = "SELECT * FROM Showtimes s INNER JOIN Movies m ON s.movie_id = m.movie_id " +
                    "WHERE m.title = ? AND s.show_date = ? AND s.show_time = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, movieTitle);
            pstmt.setDate(2, java.sql.Date.valueOf(showDate)); // Convert to the correct format
            pstmt.setTime(3, java.sql.Time.valueOf(showTime)); // Convert to the correct format

            rs = pstmt.executeQuery();
            if (rs.next()) {
                selectedShowtime = new Showtime();
                selectedShowtime.setId(rs.getInt("showtime_id"));
                selectedShowtime.setMovieId(rs.getInt("movie_id"));
                selectedShowtime.setTheaterId(rs.getInt("theater_id"));
                selectedShowtime.setDate(showDate);
                selectedShowtime.setTime(showTime);
            } else {
                throw new SQLException("No showtime found for the specified movie, date, and time.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return selectedShowtime;
    }

    public void setSelectedShowtime(Showtime selectedShowtime) {
        this.selectedShowtime = selectedShowtime;
    }
}
