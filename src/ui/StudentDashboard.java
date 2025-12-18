package ui;

import database.StudentPortalDAO;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.Course;
import models.FeeSummary;
import models.Payment;
import models.StudentInfo;

/** Student main window with info, pay and status tabs. */
public class StudentDashboard extends javax.swing.JFrame {

  private final int studentId;

  public StudentDashboard(int studentId) {
    this.studentId = studentId;
    initComponents();
    loadInfoTab();
    loadPaymentsTab();
    loadStatusTab();
    bindPayTab();
  }

  private void loadInfoTab() {
    StudentInfo info = StudentPortalDAO.loadStudentInfo(studentId);
    if (info == null) {
      lblInfoReg.setText("Reg: -");
      lblInfoName.setText("Name: -");
      lblInfoProgram.setText("Program: -");
      lblInfoTotal.setText("Total fee: 0 RWF");
      return;
    }
    lblInfoReg.setText("Reg: " + info.getRegNumber());
    lblInfoName.setText("Name: " + info.getFullName());
    lblInfoProgram.setText("Program: " + info.getProgram());
    lblInfoTotal.setText("Total fee: " + info.getTotalFee() + " RWF");

    DefaultTableModel model =
        new DefaultTableModel(new Object[] {"Course", "Price (RWF)"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    for (Course c : info.getCourses()) {
      model.addRow(new Object[] {c.getName(), c.getPrice()});
    }
    tableCourses.setModel(model);
  }

  private void loadPaymentsTab() {
    List<Payment> payments = StudentPortalDAO.loadPayments(studentId);
    DefaultTableModel model =
        new DefaultTableModel(new Object[] {"Paid On", "Amount", "Method", "Note"}, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    for (Payment p : payments) {
      model.addRow(new Object[] {p.getPaidOn(), p.getAmount(), p.getMethod(), p.getNote()});
    }
    tablePayments.setModel(model);
  }

  private void loadStatusTab() {
    FeeSummary summary = StudentPortalDAO.loadSummary(studentId);
    BigDecimal total = summary.getTotalFee();
    BigDecimal paid = summary.getTotalPaid();
    BigDecimal remaining = summary.getOutstanding();

    lblStatusAmounts.setText(
        "Total: " + total + " | Paid: " + paid + " | Remaining: " + remaining);

    String status;
    Color color;
    if (total.compareTo(BigDecimal.ZERO) == 0) {
      status = "No courses registered";
      color = Color.GRAY;
    } else if (remaining.compareTo(BigDecimal.ZERO) <= 0 && paid.compareTo(BigDecimal.ZERO) > 0) {
      status = "Fully paid";
      color = new Color(0, 128, 0); // green
    } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
      status = "Partially paid";
      color = new Color(255, 140, 0); // orange
    } else {
      status = "Not paid";
      color = Color.RED;
    }
    lblStatusText.setText(status);
    lblStatusText.setForeground(color);
  }

  private void bindPayTab() {
    btnPay.addActionListener(
        e -> {
          String amountStr = txtPayAmount.getText().trim();
          String note = txtPayNote.getText().trim();
          String method = (String) comboPayMethod.getSelectedItem();
          
          if (amountStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Enter amount to pay.");
            return;
          }
          try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
              javax.swing.JOptionPane.showMessageDialog(this, "Amount must be positive.");
              return;
            }
            boolean ok = StudentPortalDAO.recordPayment(studentId, amount, method, note);
            if (ok) {
              javax.swing.JOptionPane.showMessageDialog(
                  this, "Payment recorded via " + method + ": " + amount + " RWF");
              txtPayAmount.setText("");
              txtPayNote.setText("");
              loadPaymentsTab();
              loadStatusTab();
            } else {
              javax.swing.JOptionPane.showMessageDialog(this, "Could not record payment.");
            }
          } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Amount must be numeric.");
          }
        });
  }

  @SuppressWarnings("unchecked")
  private void initComponents() {

    tabs = new javax.swing.JTabbedPane();

    // Info tab
    panelInfo = new javax.swing.JPanel();
    lblInfoReg = new javax.swing.JLabel();
    lblInfoName = new javax.swing.JLabel();
    lblInfoProgram = new javax.swing.JLabel();
    lblInfoTotal = new javax.swing.JLabel();
    scrollCourses = new javax.swing.JScrollPane();
    tableCourses = new javax.swing.JTable();

    // Pay tab (structure only for now - payments recorded through DAO)
    panelPay = new javax.swing.JPanel();
    lblPayAmount = new javax.swing.JLabel();
    txtPayAmount = new javax.swing.JTextField();
    lblPayMethod = new javax.swing.JLabel();
    lblPayNote = new javax.swing.JLabel();
    txtPayNote = new javax.swing.JTextField();
    btnPay = new javax.swing.JButton();
    scrollPayments = new javax.swing.JScrollPane();
    tablePayments = new javax.swing.JTable();

    // Status tab
    panelStatus = new javax.swing.JPanel();
    lblStatusHeader = new javax.swing.JLabel();
    lblStatusAmounts = new javax.swing.JLabel();
    lblStatusText = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Student - Fees Portal");
    Color bg = new Color(247, 250, 255);
    Color panel = new Color(176, 224, 230);  // light sky blue
    Color accent = new Color(0, 150, 136);
    Color text = new Color(33, 37, 41);
    getContentPane().setBackground(bg);
    tabs.setBackground(new Color(236, 241, 252));
    tabs.setForeground(text);

    // Info tab layout
    panelInfo.setBackground(panel);
    lblInfoReg.setText("Reg: -");
    lblInfoReg.setForeground(text);
    lblInfoName.setText("Name: -");
    lblInfoName.setForeground(text);
    lblInfoProgram.setText("Program: -");
    lblInfoProgram.setForeground(text);
    lblInfoTotal.setText("Total fee: 0 RWF");
    lblInfoTotal.setForeground(text);

    tableCourses.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Course", "Price (RWF)"}));
    scrollCourses.setViewportView(tableCourses);
    tableCourses.setBackground(new Color(230, 248, 255));
    tableCourses.setForeground(text);

    javax.swing.GroupLayout infoLayout = new javax.swing.GroupLayout(panelInfo);
    panelInfo.setLayout(infoLayout);
    infoLayout.setHorizontalGroup(
        infoLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                infoLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        infoLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblInfoReg)
                            .addComponent(lblInfoName)
                            .addComponent(lblInfoProgram)
                            .addComponent(lblInfoTotal))
                    .addGap(40)
                    .addComponent(
                        scrollCourses,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        450,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20)));
    infoLayout.setVerticalGroup(
        infoLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                infoLayout
                    .createSequentialGroup()
                    .addGap(25)
                    .addComponent(lblInfoReg)
                    .addGap(10)
                    .addComponent(lblInfoName)
                    .addGap(10)
                    .addComponent(lblInfoProgram)
                    .addGap(10)
                    .addComponent(lblInfoTotal))
            .addGroup(
                infoLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addComponent(
                        scrollCourses,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        350,
                        javax.swing.GroupLayout.PREFERRED_SIZE)));

    // Pay tab layout
    panelPay.setBackground(panel);
    lblPayAmount.setText("Amount (RWF):");
    lblPayAmount.setForeground(text);
    lblPayMethod.setText("Payment Method:");
    lblPayMethod.setForeground(text);
    comboPayMethod = new javax.swing.JComboBox<>();
    comboPayMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"MOMO", "BK"}));
    comboPayMethod.setBackground(new Color(230, 248, 255));
    comboPayMethod.setForeground(text);
    lblPayNote.setText("Note / Reference:");
    lblPayNote.setForeground(text);
    txtPayAmount.setBackground(new Color(230, 248, 255));
    txtPayAmount.setForeground(text);
    txtPayNote.setBackground(new Color(230, 248, 255));
    txtPayNote.setForeground(text);
    btnPay.setText("Pay");
    btnPay.setBackground(accent);
    btnPay.setForeground(Color.WHITE);
    btnPay.setFocusPainted(false);

    tablePayments.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Paid On", "Amount", "Method", "Note"}));
    scrollPayments.setViewportView(tablePayments);
    tablePayments.setBackground(new Color(230, 248, 255));
    tablePayments.setForeground(text);

    javax.swing.GroupLayout payLayout = new javax.swing.GroupLayout(panelPay);
    panelPay.setLayout(payLayout);
    payLayout.setHorizontalGroup(
        payLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                payLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addGroup(
                        payLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPayAmount)
                            .addComponent(lblPayMethod)
                            .addComponent(lblPayNote)
                            .addComponent(btnPay))
                    .addGap(15)
                    .addGroup(
                        payLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPayAmount)
                            .addComponent(txtPayNote, javax.swing.GroupLayout.DEFAULT_SIZE, 180,
                                Short.MAX_VALUE)
                            .addComponent(comboPayMethod, 0, 180, Short.MAX_VALUE))
                    .addGap(30)
                    .addComponent(
                        scrollPayments,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        430,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(20)));
    payLayout.setVerticalGroup(
        payLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                payLayout
                    .createSequentialGroup()
                    .addGap(25)
                    .addGroup(
                        payLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPayAmount)
                            .addComponent(
                                txtPayAmount,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        payLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPayMethod)
                            .addComponent(
                                comboPayMethod,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12)
                    .addGroup(
                        payLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPayNote)
                            .addComponent(
                                txtPayNote,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                28,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addComponent(
                        btnPay,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        32,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(
                payLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addComponent(
                        scrollPayments,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        350,
                        javax.swing.GroupLayout.PREFERRED_SIZE)));

    // Status tab layout
    panelStatus.setBackground(panel);
    lblStatusHeader.setText("Payment Status");
    lblStatusHeader.setFont(lblStatusHeader.getFont().deriveFont(16f));
    lblStatusHeader.setForeground(text);
    lblStatusAmounts.setText("Total: 0 | Paid: 0 | Remaining: 0");
    lblStatusAmounts.setForeground(text);
    lblStatusText.setText("Not paid");
    lblStatusText.setForeground(Color.RED);

    javax.swing.GroupLayout statusLayout = new javax.swing.GroupLayout(panelStatus);
    panelStatus.setLayout(statusLayout);
    statusLayout.setHorizontalGroup(
        statusLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                statusLayout
                    .createSequentialGroup()
                    .addGap(30)
                    .addGroup(
                        statusLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatusHeader)
                            .addComponent(lblStatusAmounts)
                            .addComponent(lblStatusText))
                    .addGap(30)));
    statusLayout.setVerticalGroup(
        statusLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                statusLayout
                    .createSequentialGroup()
                    .addGap(30)
                    .addComponent(lblStatusHeader)
                    .addGap(20)
                    .addComponent(lblStatusAmounts)
                    .addGap(15)
                    .addComponent(lblStatusText)
                    .addGap(30)));

    tabs.addTab("Info", panelInfo);
    tabs.addTab("Pay Fees", panelPay);
    tabs.addTab("Status", panelStatus);

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
    GradientHeaderPanel header = new GradientHeaderPanel("Student Portal");
    getContentPane().add(header, java.awt.BorderLayout.NORTH);
    getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(null);
  }

  private javax.swing.JButton btnPay;
  private javax.swing.JComboBox<String> comboPayMethod;
  private javax.swing.JLabel lblInfoName;
  private javax.swing.JLabel lblInfoProgram;
  private javax.swing.JLabel lblInfoReg;
  private javax.swing.JLabel lblInfoTotal;
  private javax.swing.JLabel lblPayAmount;
  private javax.swing.JLabel lblPayMethod;
  private javax.swing.JLabel lblPayNote;
  private javax.swing.JLabel lblStatusAmounts;
  private javax.swing.JLabel lblStatusHeader;
  private javax.swing.JLabel lblStatusText;
  private javax.swing.JPanel panelInfo;
  private javax.swing.JPanel panelPay;
  private javax.swing.JPanel panelStatus;
  private javax.swing.JScrollPane scrollCourses;
  private javax.swing.JScrollPane scrollPayments;
  private javax.swing.JTable tableCourses;
  private javax.swing.JTable tablePayments;
  private javax.swing.JTabbedPane tabs;
  private javax.swing.JTextField txtPayAmount;
  private javax.swing.JTextField txtPayNote;
}



