package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Authentication helpers for different application roles.
 */
public final class AuthDAO {
  private AuthDAO() {}

  /** Login for registrar and accountant users. Returns user_id or -1. */
  public static int loginStaff(String username, String password, String role) {
    String query =
        "SELECT user_id FROM fees_users WHERE username = ? AND password = ? AND role = ?";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {

      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, role);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("user_id");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return -1;
  }

  /** Login for student users (by reg number). Returns student_id or -1. */
  public static int loginStudent(String regNumber, String password) {
    String query =
        "SELECT student_id FROM fees_students WHERE reg_number = ? AND password = ?";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {

      ps.setString(1, regNumber);
      ps.setString(2, password);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("student_id");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return -1;
  }
}

