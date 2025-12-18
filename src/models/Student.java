package models;

import java.math.BigDecimal;

public class Student {
  private int id;
  private String fullName;
  private String program;
  private BigDecimal totalFee;

  public Student(int id, String fullName, String program, BigDecimal totalFee) {
    this.id = id;
    this.fullName = fullName;
    this.program = program;
    this.totalFee = totalFee;
  }

  public Student(String fullName, String program, BigDecimal totalFee) {
    this.fullName = fullName;
    this.program = program;
    this.totalFee = totalFee;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
}

