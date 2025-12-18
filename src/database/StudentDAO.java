package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Student;

/**
 * Data access for student records.
 */
public final class StudentDAO {
  private StudentDAO() {
  }

  public static boolean createStudent(Student student) {
    String sql =
        "INSERT INTO fees_students (full_name, program, total_fee) "
            + "VALUES (?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps =
            conn.prepareStatement(sql, new String[] {"student_id"})) {

      ps.setString(1, student.getFullName());
      ps.setString(2, student.getProgram());
      ps.setBigDecimal(3, student.getTotalFee());

      int affected = ps.executeUpdate();
      return affected == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static boolean updateStudent(Student student) {
    String sql =
        "UPDATE fees_students SET full_name = ?, program = ?, total_fee = ? "
            + "WHERE student_id = ?";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, student.getFullName());
      ps.setString(2, student.getProgram());
      ps.setBigDecimal(3, student.getTotalFee());
      ps.setInt(4, student.getId());
      return ps.executeUpdate() == 1;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static List<Student> listStudents() {
    List<Student> students = new ArrayList<>();

    String sql =
        "SELECT student_id, full_name, program, total_fee FROM fees_students ORDER BY student_id DESC";

    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        Student student =
            new Student(
                rs.getInt("student_id"),
                rs.getString("full_name"),
                rs.getString("program"),
                rs.getBigDecimal("total_fee"));
        students.add(student);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return students;
  }
}

