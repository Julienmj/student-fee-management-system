package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Course;

/** Data helpers used by the registrar UI. */
public final class RegistrarDAO {
  private RegistrarDAO() {}

  public static List<Course> loadCoursesForProgram(String program) {
    List<Course> courses = new ArrayList<>();
    String sql =
        "SELECT course_id, program, course_name, price_rwf "
            + "FROM fees_courses WHERE program = ? AND semester = 1 ORDER BY course_name";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, program);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          courses.add(
              new Course(
                  rs.getInt("course_id"),
                  rs.getString("program"),
                  rs.getString("course_name"),
                  rs.getBigDecimal("price_rwf")));
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return courses;
  }

  /** Loads all semester 1 courses for display/management. */
  public static List<Course> loadAllCourses() {
    List<Course> courses = new ArrayList<>();
    String sql =
        "SELECT course_id, program, course_name, price_rwf "
            + "FROM fees_courses WHERE semester = 1 ORDER BY program, course_name";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        courses.add(
            new Course(
                rs.getInt("course_id"),
                rs.getString("program"),
                rs.getString("course_name"),
                rs.getBigDecimal("price_rwf")));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return courses;
  }

  public static boolean addCourse(String program, String name, BigDecimal price) {
    String sql =
        "INSERT INTO fees_courses (program, course_name, price_rwf, semester) "
            + "VALUES (?, ?, ?, 1)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, program);
      ps.setString(2, name);
      ps.setBigDecimal(3, price);
      return ps.executeUpdate() == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static boolean updateCourse(int id, String program, String name, BigDecimal price) {
    String sql =
        "UPDATE fees_courses SET program = ?, course_name = ?, price_rwf = ? WHERE course_id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, program);
      ps.setString(2, name);
      ps.setBigDecimal(3, price);
      ps.setInt(4, id);
      return ps.executeUpdate() == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static boolean deleteCourse(int id) {
    String sql = "DELETE FROM fees_courses WHERE course_id = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      return ps.executeUpdate() == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /** Creates a student and returns the generated student_id, or -1 on error. */
  public static int createStudent(String regNumber, String fullName, String program, String password) {
    String sql =
        "INSERT INTO fees_students (reg_number, full_name, program, password) "
            + "VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps =
            conn.prepareStatement(sql, new String[] {"student_id"})) {
      ps.setString(1, regNumber);
      ps.setString(2, fullName);
      ps.setString(3, program);
      ps.setString(4, password);
      int affected = ps.executeUpdate();
      if (affected == 1) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            return rs.getInt(1);
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return -1;
  }

  public static boolean enrollStudentInCourses(int studentId, List<Integer> courseIds) {
    if (courseIds.isEmpty()) {
      return false;
    }
    String sql =
        "INSERT INTO fees_enrollments (student_id, course_id) "
            + "VALUES (?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      for (Integer courseId : courseIds) {
        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.addBatch();
      }
      ps.executeBatch();
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /** Generates a reg number like 2025XXX (7 digits total) based on current max. */
  public static String generateNextRegNumber() {
    String sql =
        "SELECT MAX(reg_number) AS max_reg FROM fees_students WHERE reg_number LIKE '2025___'";
    String next = "2025001";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      if (rs.next() && rs.getString("max_reg") != null) {
        String max = rs.getString("max_reg");
        // max is like 2025XYZ -> increment XYZ
        if (max.length() == 7) {
          String suffix = max.substring(4);
          try {
            int num = Integer.parseInt(suffix);
            num++;
            next = "2025" + String.format("%03d", num);
          } catch (NumberFormatException ignored) {
            // fall back to default
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return next;
  }

  /** Computes total fee for a set of course ids. */
  public static BigDecimal computeTotalFee(List<Integer> courseIds) {
    if (courseIds.isEmpty()) {
      return BigDecimal.ZERO;
    }
    StringBuilder inClause = new StringBuilder();
    for (int i = 0; i < courseIds.size(); i++) {
      if (i > 0) {
        inClause.append(",");
      }
      inClause.append("?");
    }
    String sql =
        "SELECT SUM(price_rwf) AS total FROM fees_courses WHERE course_id IN (" + inClause + ")";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      for (int i = 0; i < courseIds.size(); i++) {
        ps.setInt(i + 1, courseIds.get(i));
      }
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next() && rs.getBigDecimal("total") != null) {
          return rs.getBigDecimal("total");
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return BigDecimal.ZERO;
  }

  /** Deletes a student by reg number (cascades to enrollments and payments). */
  public static boolean deleteStudent(String regNumber) {
    String sql = "DELETE FROM fees_students WHERE reg_number = ?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, regNumber);
      return ps.executeUpdate() == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
}


