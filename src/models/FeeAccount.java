package models;

import java.math.BigDecimal;

public class FeeAccount {
  private int studentId;
  private BigDecimal totalFee;
  private BigDecimal outstanding;

  public FeeAccount(int studentId, BigDecimal totalFee, BigDecimal outstanding) {
    this.studentId = studentId;
    this.totalFee = totalFee;
    this.outstanding = outstanding;
  }

  public int getStudentId() {
    return studentId;
  }

  public BigDecimal getTotalFee() {
    return totalFee;
  }

  public BigDecimal getOutstanding() {
    return outstanding;
  }
}

