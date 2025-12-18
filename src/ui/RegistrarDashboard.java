package ui;

import database.RegistrarDAO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import models.Course;

/** Registrar main window with student registration tab wired to the DB. */
public class RegistrarDashboard extends javax.swing.JFrame {

  private final int userId;

  public RegistrarDashboard(int userId) {
    this.userId = userId;
    initComponents();
    bindRegisterTab();
    loadCoursesForSelectedProgram();
    bindEnrolledListTab();
    reloadEnrolledTable();
    bindCatalogTab();
    reloadCatalogTable();
  }

  // ---------------- Register tab behaviour ----------------

  private void bindRegisterTab() {
    btnRegGenerateReg.addActionListener(
        (ActionEvent e) -> txtRegNumber.setText(RegistrarDAO.generateNextRegNumber()));

    btnRegRegister.addActionListener(
        (ActionEvent e) -> registerSingleStudent());

    btnRegSample.addActionListener(
        (ActionEvent e) -> generateSampleStudents());

    comboProgram.addActionListener(
        (ActionEvent e) -> loadCoursesForSelectedProgram());
  }

  private void bindEnrolledListTab() {
    btnDeleteStudent.addActionListener(
        (ActionEvent e) -> deleteSelectedStudent());
  }

  private void deleteSelectedStudent() {
    int row = tableEnrolled.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Please select a student to delete.");
      return;
    }
    String regNumber = tableEnrolled.getValueAt(row, 0).toString();
    String fullName = tableEnrolled.getValueAt(row, 1).toString();
    
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Delete student " + fullName + " (" + regNumber + ")?\nThis will also delete all enrollments and payments.",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirm != JOptionPane.YES_OPTION) {
      return;
    }
    
    boolean ok = RegistrarDAO.deleteStudent(regNumber);
    if (ok) {
      JOptionPane.showMessageDialog(this, "Student deleted successfully.");
      reloadEnrolledTable();
    } else {
      JOptionPane.showMessageDialog(this, "Could not delete student.");
    }
  }

  private void loadCoursesForSelectedProgram() {
    String program = (String) comboProgram.getSelectedItem();
    if (program == null) {
      return;
    }
    coursesPanel.removeAll();
    currentCourseCheckboxes.clear();

    List<Course> courses = RegistrarDAO.loadCoursesForProgram(program);
    coursesPanel.setLayout(new javax.swing.BoxLayout(coursesPanel, javax.swing.BoxLayout.Y_AXIS));
    for (Course c : courses) {
      JCheckBox box = new JCheckBox(c.toString());
      box.putClientProperty("courseId", c.getId());
      currentCourseCheckboxes.add(box);
      coursesPanel.add(box);
    }
    coursesPanel.revalidate();
    coursesPanel.repaint();
  }

  private void registerSingleStudent() {
    String reg = txtRegNumber.getText().trim();
    String name = txtFullName.getText().trim();
    String program = (String) comboProgram.getSelectedItem();
    String password = txtPassword.getText().trim();
    if (password.isEmpty()) {
      password = "123";
    }

    if (reg.isEmpty() || name.isEmpty() || program == null) {
      JOptionPane.showMessageDialog(this, "Reg number, name and program are required");
      return;
    }
    List<Integer> courseIds = new ArrayList<>();
    for (JCheckBox box : currentCourseCheckboxes) {
      if (box.isSelected()) {
        Object idObj = box.getClientProperty("courseId");
        if (idObj instanceof Integer) {
          courseIds.add((Integer) idObj);
        }
      }
    }
    if (courseIds.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Select at least one course");
      return;
    }

    int studentId = RegistrarDAO.createStudent(reg, name, program, password);
    if (studentId == -1) {
      JOptionPane.showMessageDialog(this, "Could not save student (maybe reg number exists).");
      return;
    }
    boolean ok = RegistrarDAO.enrollStudentInCourses(studentId, courseIds);
    if (!ok) {
      JOptionPane.showMessageDialog(this, "Student saved but course enrollment failed.");
    } else {
      BigDecimal total = RegistrarDAO.computeTotalFee(courseIds);
      JOptionPane.showMessageDialog(
          this,
          "Student registered.\nTotal fee for selected courses: " + total + " RWF");
      reloadEnrolledTable();
      clearRegisterForm();
    }
  }

  /**
   * Very simple generator for 10 sample students with random programs and 1-4 courses each. Names
   * use Kinyarwanda-like first names and foreign surnames.
   */
  private void generateSampleStudents() {
    String[] kinyFirst = {"Iradukunda", "Niyonsenga", "Uwase", "Mugisha", "Ishimwe"};
    String[] foreignLast = {"Smith", "Johnson", "Brown", "Garcia", "Williams"};
    String[] programs = {"SOFTWARE ENGINEERING", "INFO MANAGEMENT", "NETWORKING"};

    int created = 0;
    for (int i = 0; i < 10; i++) {
      String reg = RegistrarDAO.generateNextRegNumber();
      String name =
          kinyFirst[i % kinyFirst.length] + " " + foreignLast[(i * 2) % foreignLast.length];
      String program = programs[i % programs.length];

      List<Course> coursePool = RegistrarDAO.loadCoursesForProgram(program);
      if (coursePool.isEmpty()) {
        continue;
      }
      List<Integer> courseIds = new ArrayList<>();
      int maxCourses = 1 + (i % 4); // 1..4
      for (int c = 0; c < coursePool.size() && courseIds.size() < maxCourses; c++) {
        courseIds.add(coursePool.get((i + c) % coursePool.size()).getId());
      }

      int studentId = RegistrarDAO.createStudent(reg, name, program, "123");
      if (studentId != -1) {
        RegistrarDAO.enrollStudentInCourses(studentId, courseIds);
        created++;
      }
    }
    reloadEnrolledTable();
    JOptionPane.showMessageDialog(this, created + " sample students generated.");
  }

  private void clearRegisterForm() {
    txtRegNumber.setText("");
    txtFullName.setText("");
    txtPassword.setText("123");
    comboProgram.setSelectedIndex(0);
    for (JCheckBox box : currentCourseCheckboxes) {
      box.setSelected(false);
    }
  }

  private void reloadEnrolledTable() {
    String sql =
        "SELECT s.reg_number, s.full_name, s.program, "
            + "GROUP_CONCAT(c.course_name ORDER BY c.course_name SEPARATOR ', ') AS courses, "
            + "SUM(c.price_rwf) AS total_fee "
            + "FROM fees_students s "
            + "JOIN fees_enrollments e ON e.student_id = s.student_id "
            + "JOIN fees_courses c ON c.course_id = e.course_id "
            + "GROUP BY s.reg_number, s.full_name, s.program "
            + "ORDER BY s.reg_number";
    DefaultTableModel model =
        new DefaultTableModel(
            new Object[] {"Reg Number", "Full Name", "Program", "Courses", "Total Fee"}, 0);
    try (java.sql.Connection conn = database.DBConnection.getConnection();
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        java.sql.ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        model.addRow(
            new Object[] {
              rs.getString("reg_number"),
              rs.getString("full_name"),
              rs.getString("program"),
              rs.getString("courses"),
              rs.getBigDecimal("total_fee")
            });
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    tableEnrolled.setModel(model);
  }

  // ------------- Catalog tab (courses CRUD) -------------

  private void bindCatalogTab() {
    btnCourseAdd.addActionListener(
        (ActionEvent e) -> {
          String program = (String) comboCourseProgram.getSelectedItem();
          String name = txtCourseName.getText().trim();
          String priceStr = txtCoursePrice.getText().trim();
          if (program == null || name.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Program, course name and price are required.");
            return;
          }
          try {
            BigDecimal price = new BigDecimal(priceStr);
            boolean ok = RegistrarDAO.addCourse(program, name, price);
            if (ok) {
              JOptionPane.showMessageDialog(this, "Course added.");
              reloadCatalogTable();
              loadCoursesForSelectedProgram();
              txtCourseName.setText("");
              txtCoursePrice.setText("");
            } else {
              JOptionPane.showMessageDialog(this, "Could not add course.");
            }
          } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be numeric.");
          }
        });

    btnCourseUpdate.addActionListener(
        (ActionEvent e) -> {
          int row = tableCourses.getSelectedRow();
          if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course to update.");
            return;
          }
          int id = (Integer) tableCourses.getValueAt(row, 0);
          String program = (String) comboCourseProgram.getSelectedItem();
          String name = txtCourseName.getText().trim();
          String priceStr = txtCoursePrice.getText().trim();
          if (program == null || name.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Program, course name and price are required.");
            return;
          }
          try {
            BigDecimal price = new BigDecimal(priceStr);
            boolean ok = RegistrarDAO.updateCourse(id, program, name, price);
            if (ok) {
              JOptionPane.showMessageDialog(this, "Course updated.");
              reloadCatalogTable();
              loadCoursesForSelectedProgram();
            } else {
              JOptionPane.showMessageDialog(this, "Could not update course.");
            }
          } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be numeric.");
          }
        });

    btnCourseDelete.addActionListener(
        (ActionEvent e) -> {
          int row = tableCourses.getSelectedRow();
          if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course to delete.");
            return;
          }
          int id = (Integer) tableCourses.getValueAt(row, 0);
          int confirm =
              JOptionPane.showConfirmDialog(
                  this, "Delete selected course?", "Confirm", JOptionPane.YES_NO_OPTION);
          if (confirm != JOptionPane.YES_OPTION) {
            return;
          }
          boolean ok = RegistrarDAO.deleteCourse(id);
          if (ok) {
            JOptionPane.showMessageDialog(this, "Course deleted.");
            reloadCatalogTable();
            loadCoursesForSelectedProgram();
          } else {
            JOptionPane.showMessageDialog(this, "Could not delete course.");
          }
        });

    tableCourses
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              int row = tableCourses.getSelectedRow();
              if (row >= 0) {
                comboCourseProgram.setSelectedItem(tableCourses.getValueAt(row, 1));
                txtCourseName.setText(tableCourses.getValueAt(row, 2).toString());
                txtCoursePrice.setText(tableCourses.getValueAt(row, 3).toString());
              }
            });
  }

  private void reloadCatalogTable() {
    List<Course> courses = RegistrarDAO.loadAllCourses();
    DefaultTableModel model =
        new DefaultTableModel(new Object[] {"ID", "Program", "Course", "Price"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    for (Course c : courses) {
      model.addRow(new Object[] {c.getId(), c.getProgram(), c.getName(), c.getPrice()});
    }
    tableCourses.setModel(model);
  }

  // ---------------- UI boilerplate ----------------

  @SuppressWarnings("unchecked")
  private void initComponents() {

    tabs = new javax.swing.JTabbedPane();

    // Register tab controls
    panelRegister = new javax.swing.JPanel();
    lblRegNumber = new javax.swing.JLabel();
    txtRegNumber = new javax.swing.JTextField();
    btnRegGenerateReg = new javax.swing.JButton();
    lblFullName = new javax.swing.JLabel();
    txtFullName = new javax.swing.JTextField();
    lblProgram = new javax.swing.JLabel();
    comboProgram = new javax.swing.JComboBox<>();
    lblPassword = new javax.swing.JLabel();
    txtPassword = new javax.swing.JTextField();
    lblCourses = new javax.swing.JLabel();
    coursesPanel = new JPanel();
    scrollCourses = new JScrollPane(coursesPanel);
    btnRegRegister = new javax.swing.JButton();
    btnRegSample = new javax.swing.JButton();

    // Catalog + list panels are still placeholders
    panelCatalog = new javax.swing.JPanel();
    panelList = new javax.swing.JPanel();
    scrollEnrolled = new javax.swing.JScrollPane();
    tableEnrolled = new javax.swing.JTable();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Registrar - Student Fees Tracker");
    Color bg = new Color(247, 250, 255);
    Color panel = new Color(176, 224, 230);  // light sky blue
    Color accent = new Color(0, 150, 136);
    Color text = new Color(33, 37, 41);
    getContentPane().setBackground(bg);

    // --- Register tab layout ---
    panelRegister.setBackground(panel);
    panelRegister.setForeground(text);
    lblRegNumber.setText("Reg Number:");
    lblRegNumber.setForeground(text);
    btnRegGenerateReg.setText("Generate");

    lblFullName.setText("Full Name:");
    lblFullName.setForeground(text);

    lblProgram.setText("Program:");
    lblProgram.setForeground(text);
    comboProgram.setModel(
        new javax.swing.DefaultComboBoxModel<>(
            new String[] {"SOFTWARE ENGINEERING", "INFO MANAGEMENT", "NETWORKING"}));

    lblPassword.setText("Password:");
    lblPassword.setForeground(text);
    txtPassword.setText("123");

    lblCourses.setText("Courses (Sem 1):");
    lblCourses.setForeground(text);

    txtRegNumber.setBackground(new Color(230, 248, 255));
    txtRegNumber.setForeground(text);
    txtFullName.setBackground(new Color(230, 248, 255));
    txtFullName.setForeground(text);
    txtPassword.setBackground(new Color(230, 248, 255));
    txtPassword.setForeground(text);
    comboProgram.setBackground(new Color(230, 248, 255));
    comboProgram.setForeground(text);
    coursesPanel.setBackground(new Color(241, 245, 252));

    btnRegRegister.setText("Register");
    btnRegRegister.setBackground(accent);
    btnRegRegister.setForeground(Color.WHITE);
    btnRegRegister.setFocusPainted(false);
    btnRegSample.setText("Generate 10 Sample Students");
    btnRegSample.setBackground(new Color(230, 235, 242));
    btnRegSample.setForeground(text);
    btnRegSample.setFocusPainted(false);

    javax.swing.GroupLayout regLayout = new javax.swing.GroupLayout(panelRegister);
    panelRegister.setLayout(regLayout);
    regLayout.setHorizontalGroup(
        regLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                regLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegNumber)
                            .addComponent(lblFullName)
                            .addComponent(lblProgram)
                            .addComponent(lblPassword)
                            .addComponent(lblCourses)
                            .addComponent(btnRegRegister)
                            .addComponent(btnRegSample))
                    .addGap(15)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(
                                regLayout
                                    .createSequentialGroup()
                                    .addComponent(
                                        txtRegNumber,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        160,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10)
                                    .addComponent(btnRegGenerateReg))
                            .addComponent(
                                txtFullName,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                260,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboProgram, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(
                                txtPassword,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                160,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(
                                scrollCourses,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                320,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20)));
    regLayout.setVerticalGroup(
        regLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                regLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRegNumber)
                            .addComponent(
                                txtRegNumber,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegGenerateReg))
                    .addGap(12)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFullName)
                            .addComponent(
                                txtFullName,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblProgram)
                            .addComponent(
                                comboProgram,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPassword)
                            .addComponent(
                                txtPassword,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        regLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCourses)
                            .addComponent(
                                scrollCourses,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                200,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(15)
                    .addComponent(
                        btnRegRegister,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(
                        btnRegSample,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(15)));

    // --- Enrolled list tab (with delete button) ---
    panelList.setBackground(panel);
    btnDeleteStudent = new javax.swing.JButton();
    btnDeleteStudent.setText("Delete Selected Student");
    btnDeleteStudent.setBackground(new Color(200, 80, 80));
    btnDeleteStudent.setForeground(Color.WHITE);
    btnDeleteStudent.setFocusPainted(false);
    
    tableEnrolled.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Reg Number", "Full Name", "Program"}));
    scrollEnrolled.setViewportView(tableEnrolled);
    tableEnrolled.setBackground(new Color(230, 248, 255));
    tableEnrolled.setForeground(text);

    javax.swing.GroupLayout listLayout = new javax.swing.GroupLayout(panelList);
    panelList.setLayout(listLayout);
    listLayout.setHorizontalGroup(
        listLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                listLayout
                    .createSequentialGroup()
                    .addGap(15)
                    .addGroup(
                        listLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(
                                scrollEnrolled,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                760,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteStudent))
                    .addGap(15)));
    listLayout.setVerticalGroup(
        listLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                listLayout
                    .createSequentialGroup()
                    .addGap(15)
                    .addComponent(
                        scrollEnrolled,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        380,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(
                        btnDeleteStudent,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(15)));

    // --- Catalog tab layout (courses CRUD) ---
    panelCatalog.setBackground(panel);
    lblCourseProgram = new javax.swing.JLabel();
    lblCourseName = new javax.swing.JLabel();
    lblCoursePrice = new javax.swing.JLabel();
    comboCourseProgram = new javax.swing.JComboBox<>();
    txtCourseName = new javax.swing.JTextField();
    txtCoursePrice = new javax.swing.JTextField();
    btnCourseAdd = new javax.swing.JButton();
    btnCourseUpdate = new javax.swing.JButton();
    btnCourseDelete = new javax.swing.JButton();
    scrollCoursesTable = new javax.swing.JScrollPane();
    tableCourses = new javax.swing.JTable();

    lblCourseProgram.setText("Program:");
    lblCourseProgram.setForeground(text);
    lblCourseName.setText("Course name:");
    lblCourseName.setForeground(text);
    lblCoursePrice.setText("Price (RWF):");
    lblCoursePrice.setForeground(text);

    comboCourseProgram.setModel(
        new javax.swing.DefaultComboBoxModel<>(
            new String[] {"SOFTWARE ENGINEERING", "INFO MANAGEMENT", "NETWORKING"}));
    comboCourseProgram.setBackground(new Color(230, 248, 255));
    comboCourseProgram.setForeground(text);
    txtCourseName.setBackground(new Color(230, 248, 255));
    txtCourseName.setForeground(text);
    txtCoursePrice.setBackground(new Color(230, 248, 255));
    txtCoursePrice.setForeground(text);

    btnCourseAdd.setText("Add");
    btnCourseAdd.setBackground(accent);
    btnCourseAdd.setForeground(Color.WHITE);
    btnCourseAdd.setFocusPainted(false);
    btnCourseUpdate.setText("Update");
    btnCourseUpdate.setBackground(new Color(220, 224, 235));
    btnCourseUpdate.setForeground(text);
    btnCourseUpdate.setFocusPainted(false);
    btnCourseDelete.setText("Delete");
    btnCourseDelete.setBackground(new Color(200, 80, 80));
    btnCourseDelete.setForeground(Color.WHITE);
    btnCourseDelete.setFocusPainted(false);

    tableCourses.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"ID", "Program", "Course", "Price"}));
    scrollCoursesTable.setViewportView(tableCourses);
    tableCourses.setBackground(new Color(230, 248, 255));
    tableCourses.setForeground(text);

    javax.swing.GroupLayout catalogLayout = new javax.swing.GroupLayout(panelCatalog);
    panelCatalog.setLayout(catalogLayout);
    catalogLayout.setHorizontalGroup(
        catalogLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                catalogLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        catalogLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCourseProgram)
                            .addComponent(lblCourseName)
                            .addComponent(lblCoursePrice)
                            .addComponent(btnCourseAdd)
                            .addComponent(btnCourseUpdate)
                            .addComponent(btnCourseDelete))
                    .addGap(15)
                    .addGroup(
                        catalogLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboCourseProgram, 0, 220, Short.MAX_VALUE)
                            .addComponent(txtCourseName)
                            .addComponent(txtCoursePrice))
                    .addGap(20)));
    catalogLayout.setVerticalGroup(
        catalogLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                catalogLayout
                    .createSequentialGroup()
                    .addGap(25)
                    .addGroup(
                        catalogLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCourseProgram)
                            .addComponent(
                                comboCourseProgram,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        catalogLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCourseName)
                            .addComponent(
                                txtCourseName,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        catalogLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCoursePrice)
                            .addComponent(
                                txtCoursePrice,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addComponent(
                        btnCourseAdd,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        30,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(
                        btnCourseUpdate,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        30,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(
                        btnCourseDelete,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        30,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(30)));

    tabs.addTab("Register Student", panelRegister);
    tabs.addTab("Payment Info (2025)", panelCatalog);
    tabs.addTab("Enrolled List", panelList);

    javax.swing.JPanel mainPanel = new javax.swing.JPanel();
    mainPanel.setOpaque(false);
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(mainPanel);
    mainPanel.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(10)
                    .addComponent(
                        tabs,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        820,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)));
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(10)
                    .addComponent(
                        tabs,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        500,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)));

    getContentPane().setLayout(new java.awt.BorderLayout());
    GradientHeaderPanel header = new GradientHeaderPanel("Registrar Console");
    getContentPane().add(header, java.awt.BorderLayout.NORTH);
    getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(null);
  }

  // Fields
  private javax.swing.JButton btnCourseAdd;
  private javax.swing.JButton btnCourseDelete;
  private javax.swing.JButton btnCourseUpdate;
  private javax.swing.JButton btnDeleteStudent;
  private javax.swing.JButton btnRegGenerateReg;
  private javax.swing.JButton btnRegRegister;
  private javax.swing.JButton btnRegSample;
  private javax.swing.JComboBox<String> comboProgram;
  private javax.swing.JComboBox<String> comboCourseProgram;
  private JPanel coursesPanel;
  private javax.swing.JLabel lblCourses;
  private javax.swing.JLabel lblCourseProgram;
  private javax.swing.JLabel lblCourseName;
  private javax.swing.JLabel lblCoursePrice;
  private javax.swing.JLabel lblFullName;
  private javax.swing.JLabel lblPassword;
  private javax.swing.JLabel lblProgram;
  private javax.swing.JLabel lblRegNumber;
  private javax.swing.JPanel panelCatalog;
  private javax.swing.JPanel panelList;
  private javax.swing.JPanel panelRegister;
  private javax.swing.JScrollPane scrollCourses;
  private javax.swing.JScrollPane scrollCoursesTable;
  private javax.swing.JScrollPane scrollEnrolled;
  private javax.swing.JTable tableEnrolled;
  private javax.swing.JTable tableCourses;
  private javax.swing.JTabbedPane tabs;
  private javax.swing.JTextField txtFullName;
  private javax.swing.JTextField txtCourseName;
  private javax.swing.JTextField txtCoursePrice;
  private javax.swing.JTextField txtPassword;
  private javax.swing.JTextField txtRegNumber;

  private final List<JCheckBox> currentCourseCheckboxes = new ArrayList<>();
}



