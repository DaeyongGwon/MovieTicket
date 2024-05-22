import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieDetailPanel extends JPanel {
    private Movie movie;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MovieDetailPanel(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        removeAll();

        JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        JTextArea descriptionArea = new JTextArea(movie.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);

        JLabel posterLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(movie.getPosterPath());
            Image image = icon.getImage();
            double widthRatio = (double) getWidth() / image.getWidth(null);
            double heightRatio = (double) getHeight() / image.getHeight(null);
            double ratio = Math.min(widthRatio, heightRatio);
            int newWidth = (int) (image.getWidth(null) * ratio);
            int newHeight = (int) (image.getHeight(null) * ratio);
            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton reserveButton = new JButton("예매하기");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 예매 화면으로 이동
                ((ReservationPanel) mainPanel.getComponent(2)).setMovie(movie);
                cardLayout.show(mainPanel, "Reservation");
            }
        });

        JPanel posterPanel = new JPanel(new GridBagLayout());
        posterPanel.add(posterLabel);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        descriptionPanel.add(reserveButton, BorderLayout.SOUTH);

        add(titleLabel, BorderLayout.NORTH);
        add(posterPanel, BorderLayout.CENTER);
        add(descriptionPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (movie != null && movie.getPosterPath() != null) {
            try {
                ImageIcon icon = new ImageIcon(movie.getPosterPath());
                Image image = icon.getImage();
                double widthRatio = (double) getWidth() / image.getWidth(null);
                double heightRatio = (double) getHeight() / image.getHeight(null);
                double ratio = Math.min(widthRatio, heightRatio);
                int newWidth = (int) (image.getWidth(null) * ratio);
                int newHeight = (int) (image.getHeight(null) * ratio);
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                g.drawImage(scaledImage, (getWidth() - newWidth) / 2, (getHeight() - newHeight) / 2, newWidth, newHeight, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
