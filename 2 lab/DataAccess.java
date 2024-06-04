import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataAccess {
    private Connection connection;
    private PreparedStatement selectCount;
    /********************************************************/
    public static void loadDriver()
    {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException cnfe) {
            System.out.println("Couldn't find driver class!");
            cnfe.printStackTrace();
            System.exit(1);
        }
    }
    /********************************************************/
    public static Connection getConnection() {
        Connection postGresConn = null;
        try {
            postGresConn = DriverManager.getConnection("jdbc:postgresql://pgsql3.mif/studentu", "insert_name", "insert_password") ;
        }
        catch (SQLException sqle) {
            System.out.println("Couldn't connect to database!");
            sqle.printStackTrace();
            return null ;
        }
        System.out.println("Successfully connected to Postgres Database");

        return postGresConn ;
    }
    private void prepareStatements() throws SQLException{
        selectCount = connection.prepareStatement("SELECT COUNT(DISTINCT Advokato_Kodas) FROM name.Advokatai");
    }


    public DataAccess() throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        loadDriver() ;

        connection = getConnection();
        prepareStatements();
    }
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error while closing connection to database");
        }
    }
    public List<List> queryDb(String query) throws Exception{
        Statement stmt = null;
        ResultSet rs = null;
        List<List> result = new LinkedList<List>();

        try
        {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next())
            {
                List<String> row = new LinkedList<String>();
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++)
                {
                    row.add(rs.getString(i));
                }
                result.add(row);
            }
        }
        catch (SQLException e)
        {
            if (!e.getMessage().equals("No results were returned by the query."))
            {
                throw new SQLException(e.getMessage());
            }
        }
        finally
        {
            try {
                if(null != rs)
                {
                    rs.close();
                }
                if(null != stmt)
                {
                    stmt.close();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Unexpected SQL Error");
            }
        }
        return result;
    }
    public void deleteUpdate(long kodas, long kodas2) throws SQLException {
        try {
            connection.setAutoCommit(false);
            queryDb("UPDATE name.Advokatai SET Advokato_Kodas = "+ kodas2 + " WHERE Advokato_Kodas = "+kodas+";");
            queryDb("delete from name.Advokatas where Advokato_Kodas = " + kodas + ";");
            List<List> lawyers = new LinkedList<List>();
            ResultSet result = selectCount.executeQuery();
            while (result.next())
            {
                List<String> row = new LinkedList<String>();
                for (int i = 1; i < result.getMetaData().getColumnCount() + 1; i++)
                {
                    row.add(result.getString(i));
                }
                lawyers.add(row);
            }
            if(Integer.valueOf((String) lawyers.get(0).get(0)) == 0)
                throw new SQLException("Negalima istrinti paskutinio advokato");
            connection.commit();
            connection.setAutoCommit(true);
        } catch(SQLException ex) {
            System.out.println("Advokato istrinti nepavyko, atnaujinimas neivyko");
            connection.rollback();
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            connection.setAutoCommit(true);
        }
    }

}
