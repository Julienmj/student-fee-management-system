package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.FeeSummary;
import models.Payment;

/**
 * Handles fee account and payment persistence.
 */
public final class FeeAccountDAO {
  private FeeAccountDAO() {
  }

  public static boolean recordPayment(Payment payment) {
    String insertPayment =
        "INSERT INTO fees_payments (student_id, amount, method, note, paid_on) "
            + "VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(insertPayment)) {

      ps.setInt(1, payment.getStudentId());
      ps.setBigDecimal(2, payment.getAmount());
      ps.setString(3, payment.getMethod());
      ps.setString(4, payment.getNote());
      ps.setDate(5, new Date(payment.getPaidOn().getTime()));

      int affected = ps.executeUpdate();
      return affected == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static List<Payment> fetchPayments(int studentId) {
    List<Payment> payments = new ArrayList<>();
    String sql =
        "SELECT payment_id, student_id, amount, method, note, paid_on "
            + "FROM fees_payments WHERE student_id = ? ORDER BY paid_on DESC";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, studentId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Payment payment =
              new Payment(
                  rs.getInt("payment_id"),
                  rs.getInt("student_id"),
                  rs.getBigDecimal("amount"),
                  rs.getString("method"),
                  rs.getString("note"),
                  rs.getDate("paid_on"));
          payments.add(payment);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return payments;
  }

  public static FeeSummary fetchFeeSummary(int studentId) {
    String totalSql = "SELECT total_fee FROM fees_students WHERE student_id = ?";
    String paidSql = "SELECT COALESCE(SUM(amount), 0) AS total_paid FROM fees_payments WHERE student_id = ?";

    BigDecimal total = BigDecimal.ZERO;
    BigDecimal paid = BigDecimal.ZERO;

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement totalPs = conn.prepareStatement(totalSql);
        PreparedStatement paidPs = conn.prepareStatement(paidSql)) {

      totalPs.setInt(1, studentId);
      try (ResultSet rs = totalPs.executeQuery()) {
        if (rs.next()) {
          total = rs.getBigDecimal("total_fee");
        }
      }

      paidPs.setInt(1, studentId);
      try (ResultSet rs = paidPs.executeQuery()) {
        if (rs.next()) {
          paid = rs.getBigDecimal("total_paid");
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    BigDecimal outstanding = total.subtract(paid);
    return new FeeSummary(studentId, total, paid, outstanding);
  }
}

