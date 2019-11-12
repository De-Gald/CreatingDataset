import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite {
    private static final String URL = "jdbc:sqlite:SoccerStats";

    public static void main(String[] args) {
        Integer a = 34;
        insert("12/12/2019", "Prem", "Barselona", a, 13, "Baltika" );
    }

    public static void insert(String data_match, String tournament, String home_team, int home_team_corners, int away_team_corners, String away_team) {
        final String SQL = "INSERT INTO corners VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(SQL);) {
            ps.setString(1, data_match);
            ps.setString(2, tournament);
            ps.setString(3, home_team);
            ps.setInt(4, home_team_corners);
            ps.setInt(5,away_team_corners);
            ps.setString(6, away_team);
            ps.setString(7, "SpainLigaBBVA");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}