package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.Course;
import models.FeeSummary;
import models.Payment;
import models.StudentInfo;

/** Data helpers for the student-facing dashboard. */
public final class StudentPortalDAO {

  private StudentPortalDAO() {}

  public static StudentInfo loadStudentInfo(int studentId) {
    String sqlStudent =
        "SELECT reg_number, full_name, program FROM fees_students WHERE student_id = ?";
    String sqlCourses =
        "SELECT c.course_id, c.program, c.course_name, c.price_rwf "
            + "FROM fees_enrollments e "
            + "JOIN fees_courses c ON c.course_id = e.course_id "
            + "WHERE e.student_id = ? AND c.semester = 1 "
            + "ORDER BY c.course_name";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement psStudent = conn.prepareStatement(sqlStudent);
        PreparedStatement psCourses = conn.prepareStatement(sqlCourses)) {

      psStudent.setInt(1, studentId);
      String reg = null;
      String name = null;
      String program = null;
      try (ResultSet rs = psStudent.executeQuery()) {
        if (rs.next()) {
          reg = rs.getString("reg_number");
          name = rs.getString("full_name");
          program = rs.getString("program");
        } else {
          return null;
        }
      }

      psCourses.setInt(1, studentId);
      List<Course> courses = new ArrayList<>();
      BigDecimal total = BigDecimal.ZERO;
      try (ResultSet rs = psCourses.executeQuery()) {
        while (rs.next()) {
          Course c =
              new Course(
                  rs.getInt("course_id"),
                  rs.getString("program"),
                  rs.getString("course_name"),
                  rs.getBigDecimal("price_rwf"));
          courses.add(c);
          total = total.add(c.getPrice());
        }
      }

      return new StudentInfo(reg, name, program, courses, total);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static List<Payment> loadPayments(int studentId) {
    List<Payment> payments = new ArrayList<>();
    String sql =
        "SELECT payment_id, student_id, amount, method, note, paid_on "
            + "FROM fees_payments WHERE student_id = ? ORDER BY paid_on DESC";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, studentId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          payments.add(
              new Payment(
                  rs.getInt("payment_id"),
                  rs.getInt("student_id"),
                  rs.getBigDecimal("amount"),
                  rs.getString("method"),
                  rs.getString("note"),
                  rs.getDate("paid_on")));
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return payments;
  }

  public static FeeSummary loadSummary(int studentId) {
    // Re-use the same logic as FeeAccountDAO but scoped for this student.
    String totalSql = "SELECT SUM(c.price_rwf) AS total_fee "
        + "FROM fees_enrollments e "
        + "JOIN fees_courses c ON c.course_id = e.course_id "
        + "WHERE e.student_id = ?";
    String paidSql =
        "SELECT COALESCE(SUM(amount), 0) AS total_paid FROM fees_payments WHERE student_id = ?";

    BigDecimal total = BigDecimal.ZERO;
    BigDecimal paid = BigDecimal.ZERO;

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement totalPs = conn.prepareStatement(totalSql);
        PreparedStatement paidPs = conn.prepareStatement(paidSql)) {

      totalPs.setInt(1, studentId);
      try (ResultSet rs = totalPs.executeQuery()) {
        if (rs.next() && rs.getBigDecimal("total_fee") != null) {
          total = rs.getBigDecimal("total_fee");
        }
      }

      paidPs.setInt(1, studentId);
      try (ResultSet rs = paidPs.executeQuery()) {
        if (rs.next() && rs.getBigDecimal("total_paid") != null) {
          paid = rs.getBigDecimal("total_paid");
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    BigDecimal outstanding = total.subtract(paid);
    return new FeeSummary(studentId, total, paid, outstanding);
  }

  /** Records a MOMO payment for the given student. */
  public static boolean recordMomoPayment(int studentId, BigDecimal amount, String note) {
    return recordPayment(studentId, amount, "MOMO", note);
  }

  /** Records a payment with specified method (MOMO or BK). */
  public static boolean recordPayment(int studentId, BigDecimal amount, String method, String note) {
    String sql =
        "INSERT INTO fees_payments (student_id, amount, method, note, paid_on) "
            + "VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, studentId);
      ps.setBigDecimal(2, amount);
      ps.setString(3, method);
      ps.setString(4, note);
      ps.setDate(5, Date.valueOf(LocalDate.now()));
      return ps.executeUpdate() == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
}


