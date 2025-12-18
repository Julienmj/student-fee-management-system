package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.AccountantStudentSummary;
import models.Payment;

/** Data helpers for the accountant dashboard/report. */
public final class AccountantDAO {

  private AccountantDAO() {}

  /**
   * Returns one summary row per student with total fee (from enrolled courses), total paid, and
   * remaining.
   */
  public static List<AccountantStudentSummary> loadStudentSummaries() {
    List<AccountantStudentSummary> list = new ArrayList<>();

    String sql =
        "SELECT s.student_id, s.reg_number, s.full_name, s.program, "
            + "       COALESCE(SUM(c.price_rwf), 0) AS total_fee, "
            + "       (SELECT COALESCE(SUM(p.amount), 0) "
            + "          FROM fees_payments p "
            + "         WHERE p.student_id = s.student_id) AS total_paid "
            + "FROM fees_students s "
            + "LEFT JOIN fees_enrollments e ON e.student_id = s.student_id "
            + "LEFT JOIN fees_courses c ON c.course_id = e.course_id "
            + "GROUP BY s.student_id, s.reg_number, s.full_name, s.program "
            + "ORDER BY s.reg_number";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        BigDecimal totalFee = rs.getBigDecimal("total_fee");
        if (totalFee == null) {
          totalFee = BigDecimal.ZERO;
        }
        BigDecimal totalPaid = rs.getBigDecimal("total_paid");
        if (totalPaid == null) {
          totalPaid = BigDecimal.ZERO;
        }
        BigDecimal remaining = totalFee.subtract(totalPaid);

        list.add(
            new AccountantStudentSummary(
                rs.getInt("student_id"),
                rs.getString("reg_number"),
                rs.getString("full_name"),
                rs.getString("program"),
                totalFee,
                totalPaid,
                remaining));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return list;
  }

  /** Loads payment history for one student, for the detail panel. */
  public static List<Payment> loadPaymentsForStudent(int studentId) {
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
}


