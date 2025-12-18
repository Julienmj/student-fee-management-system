package ui;

import controllers.PaymentController;
import controllers.StudentController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.FeeSummary;
import models.Payment;
import models.Student;

public class MainDashboard extends javax.swing.JFrame {
  private final int loggedUserId;
  private final StudentController studentController;
  private final PaymentController paymentController;

  public MainDashboard(int userId, StudentController studentController) {
    this.loggedUserId = userId;
    this.studentController = studentController;
    this.paymentController = new PaymentController();
    initComponents();
    bindListeners();
    reloadStudents();
  }

  private void bindListeners() {
    btnSaveStudent.addActionListener(new SaveStudentListener());
    btnUpdateStudent.addActionListener(new UpdateStudentListener());
    btnRefreshStudents.addActionListener(evt -> reloadStudents());
    btnRecordPayment.addActionListener(new RecordPaymentListener());
    btnLoadPayments.addActionListener(new LoadPaymentsListener());
    btnCalcBalance.addActionListener(new CalcBalanceListener());
  }

  private void reloadStudents() {
    List<Student> students = studentController.listStudents();
    DefaultTableModel model =
        new DefaultTableModel(new Object[] {"ID", "Full Name", "Program", "Total Fee"}, 0);
    for (Student student : students) {
      model.addRow(
          new Object[] {
            student.getId(), student.getFullName(), student.getProgram(), student.getTotalFee()
          });
    }
    tableStudents.setModel(model);
  }

  private void loadPaymentsForStudent(int studentId) {
    List<Payment> payments = paymentController.loadPayments(studentId);
    DefaultTableModel model =
        new DefaultTableModel(new Object[] {"Paid On", "Amount", "Method", "Note"}, 0);
    for (Payment p : payments) {
      model.addRow(new Object[] {p.getPaidOn(), p.getAmount(), p.getMethod(), p.getNote()});
    }
    tablePayments.setModel(model);
  }

  private class SaveStudentListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String name = requireText(txtStudentName, "Full name");
      String program = requireText(txtProgram, "Program");
      BigDecimal totalFee = requireAmount(txtTotalFee, "Total fee");
      if (name == null || program == null || totalFee == null) {
        return;
      }

      boolean saved = studentController.registerStudent(name, program, totalFee);
      if (saved) {
        javax.swing.JOptionPane.showMessageDialog(btnSaveStudent, "Student registered");
        reloadStudents();
      } else {
        javax.swing.JOptionPane.showMessageDialog(btnSaveStudent, "Unable to save student");
      }
    }
  }

  private class UpdateStudentListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      Integer id = requireInteger(txtStudentId, "Student ID");
      String name = requireText(txtStudentName, "Full name");
      String program = requireText(txtProgram, "Program");
      BigDecimal totalFee = requireAmount(txtTotalFee, "Total fee");
      if (id == null || name == null || program == null || totalFee == null) {
        return;
      }

      boolean updated = studentController.updateStudent(id, name, program, totalFee);
      if (updated) {
        javax.swing.JOptionPane.showMessageDialog(btnUpdateStudent, "Student updated");
        reloadStudents();
      } else {
        javax.swing.JOptionPane.showMessageDialog(btnUpdateStudent, "Update failed");
      }
    }
  }

  private class RecordPaymentListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      Integer studentId = requireInteger(txtPayStudentId, "Student ID");
      BigDecimal amount = requireAmount(txtAmount, "Amount");
      String method = requireText(txtMethod, "Method");
      String note = txtNote.getText().trim();
      if (studentId == null || amount == null || method == null) {
        return;
      }

      Date paidOn = Date.valueOf(LocalDate.now());
      boolean recorded =
          paymentController.recordPayment(studentId, amount, method, note, paidOn);
      if (recorded) {
        javax.swing.JOptionPane.showMessageDialog(btnRecordPayment, "Payment recorded");
        loadPaymentsForStudent(studentId);
      } else {
        javax.swing.JOptionPane.showMessageDialog(btnRecordPayment, "Payment failed");
      }
    }
  }

  private class LoadPaymentsListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      Integer studentId = requireInteger(txtPayStudentId, "Student ID");
      if (studentId == null) {
        return;
      }
      loadPaymentsForStudent(studentId);
    }
  }

  private class CalcBalanceListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      Integer studentId = requireInteger(txtPayStudentId, "Student ID");
      if (studentId == null) {
        return;
      }

      FeeSummary summary = paymentController.loadSummary(studentId);
      if (summary == null) {
        javax.swing.JOptionPane.showMessageDialog(
            btnCalcBalance, "No fee record found for that student ID");
        return;
      }
      lblSummary.setText(
          "Total: "
              + summary.getTotalFee()
              + " | Paid: "
              + summary.getTotalPaid()
              + " | Outstanding: "
              + summary.getOutstanding());
    }
  }

  @SuppressWarnings("unchecked")
  private void initComponents() {

    tabbedPane = new javax.swing.JTabbedPane();
    studentPanel = new javax.swing.JPanel();
    paymentPanel = new javax.swing.JPanel();

    // Student tab controls
    lblStudentId = new javax.swing.JLabel();
    lblStudentName = new javax.swing.JLabel();
    lblProgram = new javax.swing.JLabel();
    lblTotalFee = new javax.swing.JLabel();
    txtStudentId = new javax.swing.JTextField();
    txtStudentName = new javax.swing.JTextField();
    txtProgram = new javax.swing.JTextField();
    txtTotalFee = new javax.swing.JTextField();
    btnSaveStudent = new javax.swing.JButton();
    btnUpdateStudent = new javax.swing.JButton();
    btnRefreshStudents = new javax.swing.JButton();
    scrollStudents = new javax.swing.JScrollPane();
    tableStudents = new javax.swing.JTable();

    // Payment tab controls
    lblPayStudentId = new javax.swing.JLabel();
    lblAmount = new javax.swing.JLabel();
    lblMethod = new javax.swing.JLabel();
    lblNote = new javax.swing.JLabel();
    txtPayStudentId = new javax.swing.JTextField();
    txtAmount = new javax.swing.JTextField();
    txtMethod = new javax.swing.JTextField();
    txtNote = new javax.swing.JTextField();
    btnRecordPayment = new javax.swing.JButton();
    btnLoadPayments = new javax.swing.JButton();
    btnCalcBalance = new javax.swing.JButton();
    scrollPayments = new javax.swing.JScrollPane();
    tablePayments = new javax.swing.JTable();
    lblSummary = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Student Fees Tracker - Dashboard");

    lblStudentId.setText("Student ID (for update):");
    lblStudentName.setText("Full Name:");
    lblProgram.setText("Program:");
    lblTotalFee.setText("Total Fee:");

    btnSaveStudent.setText("Register Student");
    btnUpdateStudent.setText("Update");
    btnRefreshStudents.setText("Refresh List");

    tableStudents.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"ID", "Full Name", "Program", "Total Fee"}));
    scrollStudents.setViewportView(tableStudents);

    javax.swing.GroupLayout studentLayout = new javax.swing.GroupLayout(studentPanel);
    studentPanel.setLayout(studentLayout);
    studentLayout.setHorizontalGroup(
        studentLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                studentLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        studentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStudentId)
                            .addComponent(lblStudentName)
                            .addComponent(lblProgram)
                            .addComponent(lblTotalFee)
                            .addComponent(btnSaveStudent)
                            .addComponent(btnUpdateStudent)
                            .addComponent(btnRefreshStudents))
                    .addGap(20)
                    .addGroup(
                        studentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtStudentId)
                            .addComponent(txtStudentName)
                            .addComponent(txtProgram)
                            .addComponent(txtTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(25)
                    .addComponent(
                        scrollStudents,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        420,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20)));
    studentLayout.setVerticalGroup(
        studentLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                studentLayout
                    .createSequentialGroup()
                    .addGap(25)
                    .addGroup(
                        studentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStudentId)
                            .addComponent(
                                txtStudentId, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        studentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStudentName)
                            .addComponent(
                                txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        studentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblProgram)
                            .addComponent(
                                txtProgram, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        studentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotalFee)
                            .addComponent(
                                txtTotalFee, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addComponent(btnSaveStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(btnUpdateStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(btnRefreshStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(
                studentLayout
                    .createSequentialGroup()
                    .addGap(15)
                    .addComponent(
                        scrollStudents,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        330,
                        javax.swing.GroupLayout.PREFERRED_SIZE)));

    tabbedPane.addTab("Students", studentPanel);

    lblPayStudentId.setText("Student ID:");
    lblAmount.setText("Amount:");
    lblMethod.setText("Method:");
    lblNote.setText("Note:");

    btnRecordPayment.setText("Record Payment");
    btnLoadPayments.setText("Load History");
    btnCalcBalance.setText("Calculate Balance");

    tablePayments.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Paid On", "Amount", "Method", "Note"}));
    scrollPayments.setViewportView(tablePayments);

    lblSummary.setText("Total: 0 | Paid: 0 | Outstanding: 0");

    javax.swing.GroupLayout paymentLayout = new javax.swing.GroupLayout(paymentPanel);
    paymentPanel.setLayout(paymentLayout);
    paymentLayout.setHorizontalGroup(
        paymentLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                paymentLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        paymentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPayStudentId)
                            .addComponent(lblAmount)
                            .addComponent(lblMethod)
                            .addComponent(lblNote)
                            .addComponent(btnRecordPayment)
                            .addComponent(btnLoadPayments)
                            .addComponent(btnCalcBalance)
                            .addComponent(lblSummary))
                    .addGap(20)
                    .addGroup(
                        paymentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPayStudentId)
                            .addComponent(txtAmount)
                            .addComponent(txtMethod)
                            .addComponent(txtNote, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(30)
                    .addComponent(
                        scrollPayments,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        420,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20)));
    paymentLayout.setVerticalGroup(
        paymentLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                paymentLayout
                    .createSequentialGroup()
                    .addGap(25)
                    .addGroup(
                        paymentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPayStudentId)
                            .addComponent(
                                txtPayStudentId, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        paymentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAmount)
                            .addComponent(
                                txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        paymentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMethod)
                            .addComponent(
                                txtMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        paymentLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNote)
                            .addComponent(
                                txtNote, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addComponent(btnRecordPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(btnLoadPayments, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(btnCalcBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(lblSummary))
            .addGroup(
                paymentLayout
                    .createSequentialGroup()
                    .addGap(15)
                    .addComponent(
                        scrollPayments,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        330,
                        javax.swing.GroupLayout.PREFERRED_SIZE)));

    tabbedPane.addTab("Payments", paymentPanel);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(10)
                    .addComponent(
                        tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 750,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)));
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(10)
                    .addComponent(
                        tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 420,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)));

    pack();
    setLocationRelativeTo(null);
  }

  private javax.swing.JButton btnCalcBalance;
  private javax.swing.JButton btnLoadPayments;
  private javax.swing.JButton btnRecordPayment;
  private javax.swing.JButton btnRefreshStudents;
  private javax.swing.JButton btnSaveStudent;
  private javax.swing.JButton btnUpdateStudent;
  private javax.swing.JLabel lblAmount;
  private javax.swing.JLabel lblMethod;
  private javax.swing.JLabel lblNote;
  private javax.swing.JLabel lblPayStudentId;
  private javax.swing.JLabel lblProgram;
  private javax.swing.JLabel lblStudentId;
  private javax.swing.JLabel lblStudentName;
  private javax.swing.JLabel lblSummary;
  private javax.swing.JLabel lblTotalFee;
  private javax.swing.JPanel paymentPanel;
  private javax.swing.JScrollPane scrollPayments;
  private javax.swing.JScrollPane scrollStudents;
  private javax.swing.JPanel studentPanel;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTable tablePayments;
  private javax.swing.JTable tableStudents;
  private javax.swing.JTextField txtAmount;
  private javax.swing.JTextField txtMethod;
  private javax.swing.JTextField txtNote;
  private javax.swing.JTextField txtPayStudentId;
  private javax.swing.JTextField txtProgram;
  private javax.swing.JTextField txtStudentId;
  private javax.swing.JTextField txtStudentName;
  private javax.swing.JTextField txtTotalFee;

  private Integer requireInteger(javax.swing.JTextField field, String label) {
    String raw = field.getText().trim();
    if (raw.isEmpty()) {
      javax.swing.JOptionPane.showMessageDialog(this, label + " is required");
      return null;
    }
    try {
      return Integer.parseInt(raw);
    } catch (NumberFormatException ex) {
      javax.swing.JOptionPane.showMessageDialog(this, label + " must be a number");
      return null;
    }
  }

  private BigDecimal requireAmount(javax.swing.JTextField field, String label) {
    String raw = field.getText().trim();
    if (raw.isEmpty()) {
      javax.swing.JOptionPane.showMessageDialog(this, label + " is required");
      return null;
    }
    try {
      return new BigDecimal(raw);
    } catch (NumberFormatException ex) {
      javax.swing.JOptionPane.showMessageDialog(this, label + " must be numeric");
      return null;
    }
  }

  private String requireText(javax.swing.JTextField field, String label) {
    String raw = field.getText().trim();
    if (raw.isEmpty()) {
      javax.swing.JOptionPane.showMessageDialog(this, label + " is required");
      return null;
    }
    return raw;
  }

}

