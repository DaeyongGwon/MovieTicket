// SeatSelectionPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeatSelectionPanel extends JPanel {
    private Movie movie;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JButton[][] seats = new JButton[5][5];

    public SeatSelectionPanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
    }

    public void setMovie(Movie movie, String theater, String date, String time) {
        this.movie = movie;
        removeAll();

        // 왼쪽에 영화 포스터 표시
        ImageIcon posterIcon = new ImageIcon(movie.getPosterPath());
        Image posterImage = posterIcon.getImage();
        int width = posterImage.getWidth(null);
        int height = posterImage.getHeight(null);
        double aspectRatio = (double) width / height;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxPosterWidth = (int) (screenSize.width / 3 * 0.8); // 현재 크기의 20% 줄임
        int maxPosterHeight = (int) (maxPosterWidth / aspectRatio); // 비율 유지

        Image scaledPosterImage = posterImage.getScaledInstance(maxPosterWidth, maxPosterHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledPosterIcon = new ImageIcon(scaledPosterImage);
        JLabel posterLabel = new JLabel(scaledPosterIcon);
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 주변에 여백 추가
        posterPanel.add(posterLabel, BorderLayout.CENTER);
        add(posterPanel, BorderLayout.WEST);

        // 오른쪽에 상영 정보 표시
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // 제목 폰트 설정
        infoPanel.add(titleLabel);

        JLabel theaterLabel = new JLabel("상영관: " + theater);
        infoPanel.add(theaterLabel);

        JLabel dateLabel = new JLabel("날짜: " + date);
        infoPanel.add(dateLabel);

        JLabel timeLabel = new JLabel("상영시간: " + time);
        infoPanel.add(timeLabel);

        add(infoPanel, BorderLayout.NORTH);

        // 오른쪽에 좌석 선택 화면 표시
        JPanel seatPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                seats[i][j] = new JButton();
                seats[i][j].setBackground(Color.RED); // 빈 좌석
                seats[i][j].setPreferredSize(new Dimension(20, 20)); // 좌석 크기 조정
                seats[i][j].addActionListener(new SeatSelectionListener(i, j));
                seatPanel.add(seats[i][j]);
            }
        }

        add(seatPanel, BorderLayout.CENTER);
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
                selectedSeat.setBackground(Color.GRAY); // 예매된 좌석
            } else {
                selectedSeat.setBackground(Color.RED); // 빈 좌석으로 되돌리기
            }
        }
    }
}
