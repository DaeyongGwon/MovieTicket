import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReservationPanel extends JPanel {
    private Movie movie;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel movieImageLabel;
    private JList<String> theaterList;
    private JList<String> dateList;
    private JList<String> timeList;
    private String selectedTheater;
    private String selectedDate;
    private String selectedTime;

    // 각 상영관별로 날짜와 시간을 저장하는 맵
    private static final String[][] THEATER_SCHEDULE = {
            {"상영관 1", "2024-05-23", "2024-05-24", "2024-05-25"},
            {"상영관 2", "2024-05-26", "2024-05-27", "2024-05-28"},
            {"상영관 3", "2024-05-29", "2024-05-30", "2024-05-31"}
    };

    public ReservationPanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // 배경색을 흰색으로 설정

        // 전체 패널에 BorderLayout 적용
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE); // 중앙 패널의 배경색을 흰색으로 설정
        add(centerPanel, BorderLayout.CENTER);

        // 이미지를 담는 패널 생성
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Color.WHITE); // 이미지 패널의 배경색을 흰색으로 설정
        centerPanel.add(imagePanel, BorderLayout.NORTH);

        // 영화 이미지 표시 레이블
        movieImageLabel = new JLabel();
        imagePanel.add(movieImageLabel);

        // 선택 창을 담는 패널 생성
        JPanel selectionPanel = new JPanel(new GridLayout(1, 3)); // 가로로 나열되도록 GridLayout 사용
        selectionPanel.setBackground(new Color(255, 240, 245)); // 선택 패널의 배경색을 아이보리색으로 설정
        centerPanel.add(selectionPanel, BorderLayout.CENTER);

        // 상영관 선택 목록
        theaterList = new JList<>(new String[]{"상영관 1", "상영관 2", "상영관 3"});
        theaterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theaterList.addListSelectionListener(e -> updateDate());
        JScrollPane theaterScrollPane = new JScrollPane(theaterList);
        selectionPanel.add(createTitledPanel(theaterScrollPane, "상영관 선택"));

        // 날짜 선택 목록
        dateList = new JList<>();
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dateList.addListSelectionListener(e -> updateTime());
        JScrollPane dateScrollPane = new JScrollPane(dateList);
        selectionPanel.add(createTitledPanel(dateScrollPane, "날짜 선택"));

        // 시간 선택 목록
        timeList = new JList<>();
        timeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timeScrollPane = new JScrollPane(timeList);
        selectionPanel.add(createTitledPanel(timeScrollPane, "시간 선택"));

        JButton selectSeatButton = new JButton("좌석 선택");
        selectSeatButton.addActionListener(e -> {
            ((SeatSelectionPanel) mainPanel.getComponent(3)).setMovie(movie, selectedTheater, selectedDate, selectedTime);
            cardLayout.show(mainPanel, "SeatSelection");
        });
        add(selectSeatButton, BorderLayout.SOUTH);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        // 영화 이미지 업데이트
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

    private void updateDate() {
        selectedTheater = theaterList.getSelectedValue();
        DefaultListModel<String> model = new DefaultListModel<>();
        if (selectedTheater != null) {
            for (String[] schedule : THEATER_SCHEDULE) {
                if (schedule[0].equals(selectedTheater)) {
                    for (int i = 1; i < schedule.length; i++) {
                        model.addElement(schedule[i]);
                    }
                    break;
                }
            }
        }
        dateList.setModel(model);
    }

    private void updateTime() {
        selectedDate = dateList.getSelectedValue();
        DefaultListModel<String> model = new DefaultListModel<>();
        if (selectedDate != null) {
            // 각 상영관과 날짜에 따른 가능한 시간을 가정하여 임의의 데이터 생성
            String[] times = {"10:00", "14:00", "18:00", "22:00"};
            for (String time : times) {
                model.addElement(time);
            }
        }
        timeList.setModel(model);
    }

    // 선택 창에 테두리와 이름을 가진 패널을 생성하는 메서드
    private JPanel createTitledPanel(Component component, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(new Color(255, 240, 245)); // 아이보리색 배경으로 설정
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
