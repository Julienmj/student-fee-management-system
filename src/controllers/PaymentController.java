package controllers;

import database.FeeAccountDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import models.FeeSummary;
import models.Payment;

/**
 * Handles payment and fee summary logic.
 */
public class PaymentController {

  public boolean recordPayment(int studentId, BigDecimal amount, String method, String note,
      Date paidOn) {
    Payment payment = new Payment(studentId, amount, method, note, paidOn);
    return FeeAccountDAO.recordPayment(payment);
  }

  public List<Payment> loadPayments(int studentId) {
    return FeeAccountDAO.fetchPayments(studentId);
  }

  public FeeSummary loadSummary(int studentId) {
    return FeeAccountDAO.fetchFeeSummary(studentId);
  }
}

