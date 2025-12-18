package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton helper for MySQL JDBC connections (XAMPP).
 */
public final class DBConnection {
  // MySQL connection settings for XAMPP
  // Default XAMPP MySQL runs on localhost:3306
  private static final String URL = "jdbc:mysql://localhost:3306/student_fees_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
  private static final String USERNAME = "root";  // Default XAMPP MySQL username
  private static final String PASSWORD = "";      // Default XAMPP MySQL password is empty

  private static Connection sharedConnection;

  private DBConnection() {
  }

  public static synchronized Connection getConnection() throws SQLException {
    if (sharedConnection == null || sharedConnection.isClosed()) {
      // Load MySQL JDBC driver
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        throw new SQLException("MySQL JDBC Driver not found", e);
      }
      sharedConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    return sharedConnection;
  }

  public static synchronized void closeQuietly() {
    if (sharedConnection != null) {
      try {
        sharedConnection.close();
      } catch (SQLException ignored) {
        // ignore close failure
      }
      sharedConnection = null;
    }
  }
}

