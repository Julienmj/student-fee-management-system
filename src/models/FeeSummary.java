package models;

import java.math.BigDecimal;

public class FeeSummary {
  private final int studentId;
  private final BigDecimal totalFee;
  private final BigDecimal totalPaid;
  private final BigDecimal outstanding;

  public FeeSummary(int studentId, BigDecimal totalFee, BigDecimal totalPaid,
      BigDecimal outstanding) {
    this.studentId = studentId;
    this.totalFee = totalFee;
    this.totalPaid = totalPaid;
    this.outstanding = outstanding;
  }

  public int getStudentId() {
    return studentId;
  }

  public BigDecimal getTotalFee() {
    return totalFee;
  }

  public BigDecimal getTotalPaid() {
    return totalPaid;
  }

  public BigDecimal getOutstanding() {
    return outstanding;
  }
}

