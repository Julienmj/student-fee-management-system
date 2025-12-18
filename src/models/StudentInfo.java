package models;

import java.math.BigDecimal;
import java.util.List;

/** Aggregated view of a student's profile and enrolled courses. */
public class StudentInfo {
  private final String regNumber;
  private final String fullName;
  private final String program;
  private final List<Course> courses;
  private final BigDecimal totalFee;

  public StudentInfo(
      String regNumber, String fullName, String program, List<Course> courses, BigDecimal totalFee) {
    this.regNumber = regNumber;
    this.fullName = fullName;
    this.program = program;
    this.courses = courses;
    this.totalFee = totalFee;
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

  public List<Course> getCourses() {
    return courses;
  }

  public BigDecimal getTotalFee() {
    return totalFee;
  }
}


