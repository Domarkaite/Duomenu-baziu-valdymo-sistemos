import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            new Front().start();
        } catch (ClassNotFoundException e) {
            System.out.println("Connection to database failed.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to database failed.");
            e.printStackTrace();
        }
    }
}
