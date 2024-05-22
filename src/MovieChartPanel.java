import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MovieChartPanel extends JPanel {
    private List<Movie> movies; // 실시간 무비 차트를 받아오는 리스트
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MovieChartPanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.movies = getRealTimeMovieChart(); // 무비 차트 데이터 가져오기
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // FlowLayout으로 변경
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < 3; i++) {
            Movie movie = movies.get(i);
            JPanel moviePanel = new JPanel(new BorderLayout());

            JLabel movieImageLabel = new JLabel();
            try {
                BufferedImage image = ImageIO.read(new File(movie.getPosterPath()));
                Image scaledImage = image.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(scaledImage);
                movieImageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JLabel movieTitleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);

            movieImageLabel.setToolTipText("상세보기 및 예매하기");
            movieImageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 상세보기 화면으로 이동
                    ((MovieDetailPanel) mainPanel.getComponent(1)).setMovie(movie);
                    cardLayout.show(mainPanel, "MovieDetail");
                }
            });

            moviePanel.add(movieImageLabel, BorderLayout.CENTER);
            moviePanel.add(movieTitleLabel, BorderLayout.SOUTH);

            add(moviePanel);
        }
    }

    private List<Movie> getRealTimeMovieChart() {
        // 무비 차트 데이터 받아오는 로직 구현 (예: API 호출)
        String imgPath = "/Users/dae/Desktop/Java/workspace/MovieTicket/images/";
        return List.of(
                new Movie("극장판 하이큐!! 쓰레기장의 결전", "영화 1 설명", imgPath+"poster1.jpeg"),
                new Movie("퓨리오사-매드맥스 사가", "영화 2 설명", imgPath+"poster2.jpeg"),
                new Movie("가필드 더 무비", "영화 3 설명", imgPath+"poster3.jpeg")
        );
    }
}
