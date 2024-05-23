import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int movieId;
    private String title;
    private String description;
    private String genre;
    private String director;
    private int duration;
    private String posterPath;
    private DatabaseManager dbManager;


    // 기본 생성자
    public Movie() {}

    // 영화 정보를 데이터베이스에서 가져오는 메서드
    public static Movie getMovieById(int movieId, DatabaseManager dbManager) {
        Movie movie = null;
        String query = "SELECT * FROM Movies WHERE movie_id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                movie = new Movie();
                movie.movieId = rs.getInt("movie_id");
                movie.title = rs.getString("title");
                movie.description = rs.getString("description");
                movie.genre = rs.getString("genre");
                movie.director = rs.getString("director");
                movie.duration = rs.getInt("duration");
                movie.posterPath = rs.getString("poster_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    // 모든 영화 정보를 데이터베이스에서 가져오는 메서드
    public static List<Movie> getAllMovies(DatabaseManager dbManager) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movies";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.movieId = rs.getInt("movie_id");
                movie.title = rs.getString("title");
                movie.description = rs.getString("description");
                movie.genre = rs.getString("genre");
                movie.director = rs.getString("director");
                movie.duration = rs.getInt("duration");
                movie.posterPath = rs.getString("poster_path");
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
