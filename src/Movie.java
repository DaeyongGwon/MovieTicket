public class Movie {
    private String title;
    private String description;
    private String posterPath;

    public Movie(String title, String description, String posterPath) {
        this.title = title;
        this.description = description;
        this.posterPath = posterPath;
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
