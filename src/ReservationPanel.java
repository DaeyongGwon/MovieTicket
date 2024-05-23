import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationPanel extends JPanel {
    private Movie movie;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel movieImageLabel;
    private JList<String> theaterList;
    private JList<String> dateList;
    private JList<String> timeList;
    private DatabaseManager dbManager;

    public ReservationPanel(JPanel mainPanel, CardLayout cardLayout, DatabaseManager dbManager) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.dbManager = dbManager;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        add(centerPanel, BorderLayout.CENTER);

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Color.WHITE);
        centerPanel.add(imagePanel, BorderLayout.NORTH);

        movieImageLabel = new JLabel();
        imagePanel.add(movieImageLabel);

        JPanel selectionPanel = new JPanel(new GridLayout(1, 3));
        selectionPanel.setBackground(new Color(255, 240, 245));
        centerPanel.add(selectionPanel, BorderLayout.CENTER);

        theaterList = new JList<>();
        theaterList.addListSelectionListener(e -> updateDates());
        JScrollPane theaterScrollPane = new JScrollPane(theaterList);
        selectionPanel.add(createTitledPanel(theaterScrollPane, "상영관 선택"));

        dateList = new JList<>();
        dateList.addListSelectionListener(e -> updateTimes());
        JScrollPane dateScrollPane = new JScrollPane(dateList);
        selectionPanel.add(createTitledPanel(dateScrollPane, "날짜 선택"));

        timeList = new JList<>();
        JScrollPane timeScrollPane = new JScrollPane(timeList);
        selectionPanel.add(createTitledPanel(timeScrollPane, "시간 선택"));

        JButton selectSeatButton = new JButton("좌석 선택");
        selectSeatButton.addActionListener(e -> {
            String selectedTheater = theaterList.getSelectedValue();
            String selectedDate = dateList.getSelectedValue();
            String selectedTime = timeList.getSelectedValue();

            if (selectedDate != null && selectedTime != null) {
                for (Component component : mainPanel.getComponents()) {
                    if (component instanceof SeatSelectionPanel) {
                        SeatSelectionPanel seatSelectionPanel = (SeatSelectionPanel) component;
                        try {
                            SeatSelectionPanel.Showtime selectedShowtime = seatSelectionPanel.getSelectedShowtime(
                                    movie.getTitle(), LocalDate.parse(selectedDate), LocalTime.parse(selectedTime));
                            seatSelectionPanel.setSelectedShowtime(selectedShowtime);
                            seatSelectionPanel.setMovie(movie, selectedTheater, selectedDate, selectedTime);
                            cardLayout.show(mainPanel, "SeatSelection");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(ReservationPanel.this, "날짜와 시간을 선택해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            }
        });

        add(selectSeatButton, BorderLayout.SOUTH);

        updateTheaters();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        try {
            ImageIcon icon = new ImageIcon(movie.getPosterPath());
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            movieImageLabel.setIcon(new ImageIcon(scaledImage));

        } catch (Exception e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    private void updateTheaters() {
        List<String> theaters = dbManager.getTheaters();
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String theater : theaters) {
            model.addElement(theater);
        }
        theaterList.setModel(model);
    }

    private void updateDates() {
        String selectedTheater = theaterList.getSelectedValue();
        if (selectedTheater != null) {
            List<String> dates = dbManager.getDates(selectedTheater);
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String date : dates) {
                model.addElement(date);
            }
            dateList.setModel(model);
        }
    }

    private void updateTimes() {
        String selectedTheater = theaterList.getSelectedValue();
        String selectedDate = dateList.getSelectedValue();
        if (selectedTheater != null && selectedDate != null) {
            List<String> times = dbManager.getTimes(selectedTheater, selectedDate);
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String time : times) {
                model.addElement(time);
            }
            timeList.setModel(model);
        }
    }

    private JPanel createTitledPanel(Component component, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(new Color(255, 240, 245));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
