package models;

import java.math.BigDecimal;

/** Aggregate row for accountant report: one per student. */
public class AccountantStudentSummary {
  private final int studentId;
  private final String regNumber;
  private final String fullName;
  private final String program;
  private final BigDecimal totalFee;
  private final BigDecimal totalPaid;
  private final BigDecimal remaining;

  public AccountantStudentSummary(
      int studentId,
      String regNumber,
      String fullName,
      String program,
      BigDecimal totalFee,
      BigDecimal totalPaid,
      BigDecimal remaining) {
    this.studentId = studentId;
    this.regNumber = regNumber;
    this.fullName = fullName;
    this.program = program;
    this.totalFee = totalFee;
    this.totalPaid = totalPaid;
    this.remaining = remaining;
  }

  public int getStudentId() {
    return studentId;
  }

  public String getRegNumber() {
    return regNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public String getProgram() {
    return program;
  }

  public BigDecimal getTotalFee() {
    return totalFee;
  }

  public BigDecimal getTotalPaid() {
    return totalPaid;
  }

  public BigDecimal getRemaining() {
    return remaining;
  }
}


