package controllers;

import database.StudentDAO;
import java.math.BigDecimal;
import java.util.List;
import models.Student;

/**
 * Handles student-related business logic.
 */
public class StudentController {

  public boolean registerStudent(String fullName, String program, BigDecimal totalFee) {
    Student student = new Student(fullName, program, totalFee);
    return StudentDAO.createStudent(student);
  }

  public boolean updateStudent(int id, String fullName, String program, BigDecimal totalFee) {
    Student student = new Student(id, fullName, program, totalFee);
    return StudentDAO.updateStudent(student);
  }

  public List<Student> listStudents() {
    return StudentDAO.listStudents();
  }
}

