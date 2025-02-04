package util;

public class DBPropertyUtil {

    // Static method to return the database connection string
    public static String getConnectionString() {
        // Define the database connection string directly
        String host = "localhost"; // Database host
        String port = "3306"; // Database port
        String dbName = "financeDB"; // Database name
        String user = "root"; // Database username
        String password = "srishtyAg12@"; // Database password

        // Return the complete connection string for MySQL
        return "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?user=" + user + "&password=" + password;
    }
}


