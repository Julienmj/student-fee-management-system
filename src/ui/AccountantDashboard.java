package ui;

import database.AccountantDAO;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.AccountantStudentSummary;
import models.Payment;

/** Accountant main window - report tab wired to DB plus simple print placeholder. */
public class AccountantDashboard extends javax.swing.JFrame {

  private final int userId;

  public AccountantDashboard(int userId) {
    this.userId = userId;
    initComponents();
    loadReportTable();
    bindReportSelection();
  }

  private void loadReportTable() {
    List<AccountantStudentSummary> rows = AccountantDAO.loadStudentSummaries();
    DefaultTableModel model =
        new DefaultTableModel(
            new Object[] {
              "Student ID", "Reg Number", "Full Name", "Program", "Total", "Paid", "Remaining",
            },
            0) {
          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    for (AccountantStudentSummary s : rows) {
      model.addRow(
          new Object[] {
            s.getStudentId(),
            s.getRegNumber(),
            s.getFullName(),
            s.getProgram(),
            s.getTotalFee(),
            s.getTotalPaid(),
            s.getRemaining()
          });
    }
    tableSummary.setModel(model);

    // Hide internal studentId column
    tableSummary.getColumnModel().getColumn(0).setMinWidth(0);
    tableSummary.getColumnModel().getColumn(0).setMaxWidth(0);

    // Color rows based on status
    tableSummary.setDefaultRenderer(
        Object.class,
        new DefaultTableCellRenderer() {
          @Override
          public Component getTableCellRendererComponent(
              JTable table,
              Object value,
              boolean isSelected,
              boolean hasFocus,
              int row,
              int column) {
            Component c =
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Object totalObj = table.getValueAt(row, 4);
            Object paidObj = table.getValueAt(row, 5);
            Object remainingObj = table.getValueAt(row, 6);
            c.setForeground(Color.BLACK);
            if (!isSelected) {
              c.setBackground(new Color(230, 248, 255));  // light sky blue
              try {
                java.math.BigDecimal total =
                    (totalObj instanceof java.math.BigDecimal)
                        ? (java.math.BigDecimal) totalObj
                        : new java.math.BigDecimal(totalObj.toString());
                java.math.BigDecimal paid =
                    (paidObj instanceof java.math.BigDecimal)
                        ? (java.math.BigDecimal) paidObj
                        : new java.math.BigDecimal(paidObj.toString());
                java.math.BigDecimal remaining =
                    (remainingObj instanceof java.math.BigDecimal)
                        ? (java.math.BigDecimal) remainingObj
                        : new java.math.BigDecimal(remainingObj.toString());
                if (total.compareTo(java.math.BigDecimal.ZERO) == 0) {
                  c.setBackground(new Color(230, 230, 230)); // gray
                } else if (remaining.compareTo(java.math.BigDecimal.ZERO) <= 0
                    && paid.compareTo(java.math.BigDecimal.ZERO) > 0) {
                  c.setBackground(new Color(200, 255, 200)); // green
                } else if (paid.compareTo(java.math.BigDecimal.ZERO) > 0) {
                  c.setBackground(new Color(255, 235, 200)); // orange
                } else {
                  c.setBackground(new Color(255, 200, 200)); // red
                }
              } catch (Exception ignored) {
              }
            } else {
              c.setBackground(new Color(184, 207, 229));
            }
            return c;
          }
        });
  }

  private void bindReportSelection() {
    tableSummary
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              int row = tableSummary.getSelectedRow();
              if (row < 0) {
                return;
              }
              int studentId = (Integer) tableSummary.getValueAt(row, 0);
              loadPaymentsFor(studentId);
            });
  }

  private void loadPaymentsFor(int studentId) {
    List<Payment> payments = AccountantDAO.loadPaymentsForStudent(studentId);
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
    tableDetails.setModel(model);
  }

  @SuppressWarnings("unchecked")
  private void initComponents() {

    tabs = new javax.swing.JTabbedPane();
    panelReport = new javax.swing.JPanel();
    panelPrint = new javax.swing.JPanel();
    btnPrintSummary = new javax.swing.JButton();
    scrollSummary = new javax.swing.JScrollPane();
    tableSummary = new javax.swing.JTable();
    scrollDetails = new javax.swing.JScrollPane();
    tableDetails = new javax.swing.JTable();
    lblHint = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Accountant - Fees Reports");
    Color bg = new Color(247, 250, 255);
    Color panel = new Color(176, 224, 230);  // light sky blue
    Color accent = new Color(0, 150, 136);
    Color text = new Color(33, 37, 41);
    getContentPane().setBackground(bg);
    tabs.setBackground(new Color(236, 241, 252));
    tabs.setForeground(text);

    // Report tab
    panelReport.setBackground(panel);
    tableSummary.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Student ID", "Reg Number", "Full Name"}));
    scrollSummary.setViewportView(tableSummary);
    tableSummary.setBackground(new Color(230, 248, 255));
    tableSummary.setForeground(text);

    tableDetails.setModel(
        new javax.swing.table.DefaultTableModel(
            new Object[][] {}, new String[] {"Paid On", "Amount", "Method", "Note"}));
    scrollDetails.setViewportView(tableDetails);
    tableDetails.setBackground(new Color(230, 248, 255));
    tableDetails.setForeground(text);

    lblHint.setText("Click a student on the left to see payment details on the right.");
    lblHint.setForeground(text);

    javax.swing.GroupLayout reportLayout = new javax.swing.GroupLayout(panelReport);
    panelReport.setLayout(reportLayout);
    reportLayout.setHorizontalGroup(
        reportLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                reportLayout
                    .createSequentialGroup()
                    .addGap(10)
                    .addComponent(
                        scrollSummary,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        430,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(
                        scrollDetails,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        320,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10))
            .addGroup(
                reportLayout
                    .createSequentialGroup()
                    .addGap(10)
                    .addComponent(lblHint)
                    .addGap(10)));
    reportLayout.setVerticalGroup(
        reportLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                reportLayout
                    .createSequentialGroup()
                    .addGap(10)
                    .addComponent(lblHint)
                    .addGap(5)
                    .addGroup(
                        reportLayout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(
                                scrollSummary,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                400,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(
                                scrollDetails,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                400,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10)));

    tabs.addTab("Report", panelReport);

    // Print tab - simple button that prints the summary table from the report tab
    panelPrint.setBackground(panel);
    btnPrintSummary.setText("Print current report");
    btnPrintSummary.setBackground(accent);
    btnPrintSummary.setForeground(Color.WHITE);
    btnPrintSummary.setFocusPainted(false);
    btnPrintSummary.addActionListener(
        e -> {
          try {
            boolean done =
                tableSummary.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Student Fees Report"),
                    new java.text.MessageFormat("Page - {0}"));
            if (!done) {
              javax.swing.JOptionPane.showMessageDialog(
                  this, "Printing was cancelled.", "Print", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
          } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(
                this, "Unable to print report: " + ex.getMessage(), "Print Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
          }
        });

    javax.swing.GroupLayout printLayout = new javax.swing.GroupLayout(panelPrint);
    panelPrint.setLayout(printLayout);
    printLayout.setHorizontalGroup(
        printLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                printLayout
                    .createSequentialGroup()
                    .addGap(30)
                    .addComponent(
                        btnPrintSummary,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        200,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(30)));
    printLayout.setVerticalGroup(
        printLayout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                printLayout
                    .createSequentialGroup()
                    .addGap(40)
                    .addComponent(
                        btnPrintSummary,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        40,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(40)));

    tabs.addTab("Print", panelPrint);

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
    GradientHeaderPanel header = new GradientHeaderPanel("Accountant Reports");
    getContentPane().add(header, java.awt.BorderLayout.NORTH);
    getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(null);
  }

  private javax.swing.JLabel lblHint;
  private javax.swing.JButton btnPrintSummary;
  private javax.swing.JPanel panelPrint;
  private javax.swing.JPanel panelReport;
  private javax.swing.JScrollPane scrollDetails;
  private javax.swing.JScrollPane scrollSummary;
  private javax.swing.JTable tableDetails;
  private javax.swing.JTable tableSummary;
  private javax.swing.JTabbedPane tabs;
}


