import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public List<String> getTheaters() {
        List<String> theaters = new ArrayList<>();
        String query = "SELECT theater_name FROM theaters";
        System.out.println(query);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                theaters.add(rs.getString("theater_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theaters;
    }


    public List<String> getDates(String theaterName) {
        List<String> dates = new ArrayList<>();
        String query = "SELECT DISTINCT show_date FROM showtimes WHERE theater_id = (SELECT theater_id FROM theaters WHERE theater_name = ?)";
        System.out.println(query);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, theaterName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dates.add(rs.getString("show_date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }

    public List<String> getTimes(String theaterName, String date) {
        List<String> times = new ArrayList<>();
        String query = "SELECT show_time FROM showtimes WHERE theater_id = (SELECT theater_id FROM theaters WHERE theater_name = ?) AND show_date = ?";
        System.out.println(query);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, theaterName);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    times.add(rs.getString("show_time"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }

}
