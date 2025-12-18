package models;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
  private int id;
  private int studentId;
  private BigDecimal amount;
  private String method;
  private String note;
  private Date paidOn;

  public Payment(
      int id, int studentId, BigDecimal amount, String method, String note, Date paidOn) {
    this.id = id;
    this.studentId = studentId;
    this.amount = amount;
    this.method = method;
    this.note = note;
    this.paidOn = paidOn;
  }

  public Payment(int studentId, BigDecimal amount, String method, String note, Date paidOn) {
    this.studentId = studentId;
    this.amount = amount;
    this.method = method;
    this.note = note;
    this.paidOn = paidOn;
  }

  public int getId() {
    return id;
  }

  public int getStudentId() {
    return studentId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getMethod() {
    return method;
  }

  public String getNote() {
    return note;
  }

  public Date getPaidOn() {
    return paidOn;
  }
}

