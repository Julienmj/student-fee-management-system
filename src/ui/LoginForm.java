package ui;

import controllers.StudentController;
import database.AuthDAO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Multi-role login form: Student, Registrar, Accountant.
 */
public class LoginForm extends javax.swing.JFrame {

  public LoginForm() {
    initComponents();
    addListeners();
  }

  private void addListeners() {
    btnLogin.addActionListener(new LoginListener());
  }

  private class LoginListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent evt) {
      String username = txtUser.getText().trim();
      String password = new String(txtPass.getPassword());

      if (username.isEmpty() || password.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
            btnLogin, "Please enter username and password");
        return;
      }

      if (radRegistrar.isSelected()) {
        int id = AuthDAO.loginStaff(username, password, "REGISTRAR");
        if (id != -1) {
          javax.swing.JOptionPane.showMessageDialog(btnLogin, "Welcome registrar!");
          RegistrarDashboard ui = new RegistrarDashboard(id);
          ui.setVisible(true);
          LoginForm.this.dispose();
        } else {
          javax.swing.JOptionPane.showMessageDialog(btnLogin, "Invalid registrar credentials");
        }
      } else if (radAccountant.isSelected()) {
        int id = AuthDAO.loginStaff(username, password, "ACCOUNTANT");
        if (id != -1) {
          javax.swing.JOptionPane.showMessageDialog(btnLogin, "Welcome accountant!");
          AccountantDashboard ui = new AccountantDashboard(id);
          ui.setVisible(true);
          LoginForm.this.dispose();
        } else {
          javax.swing.JOptionPane.showMessageDialog(btnLogin, "Invalid accountant credentials");
        }
      } else if (radStudent.isSelected()) {
        int studentId = AuthDAO.loginStudent(username, password);
        if (studentId != -1) {
          javax.swing.JOptionPane.showMessageDialog(btnLogin, "Welcome student!");
          StudentDashboard ui = new StudentDashboard(studentId);
          ui.setVisible(true);
          LoginForm.this.dispose();
        } else {
          javax.swing.JOptionPane.showMessageDialog(btnLogin, "Invalid student credentials");
        }
      } else {
        javax.swing.JOptionPane.showMessageDialog(btnLogin, "Please choose a role to log in");
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void initComponents() {

    roleGroup = new javax.swing.ButtonGroup();
    jLabelTitle = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    txtUser = new javax.swing.JTextField();
    txtPass = new javax.swing.JPasswordField();
    btnLogin = new javax.swing.JButton();
    radStudent = new javax.swing.JRadioButton();
    radRegistrar = new javax.swing.JRadioButton();
    radAccountant = new javax.swing.JRadioButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Student Fees Tracker - Login");

    Color bg = new Color(247, 250, 255);
    Color accent = new Color(0, 150, 136);      // teal
    Color text = new Color(33, 37, 41);

    getContentPane().setBackground(bg);

    jLabelTitle.setText("Login as:");
    jLabelTitle.setFont(jLabelTitle.getFont().deriveFont(16f));
    jLabelTitle.setForeground(text);

    roleGroup.add(radStudent);
    radStudent.setText("Student");
    radStudent.setForeground(text);
    roleGroup.add(radRegistrar);
    radRegistrar.setText("Registrar");
    radRegistrar.setForeground(text);
    roleGroup.add(radAccountant);
    radAccountant.setText("Accountant");
    radAccountant.setForeground(text);

    jLabel1.setText("Username:");
    jLabel1.setForeground(text);
    jLabel2.setText("Password:");
    jLabel2.setForeground(text);

    txtUser.setBackground(new Color(230, 248, 255));
    txtUser.setForeground(text);
    txtPass.setBackground(new Color(230, 248, 255));
    txtPass.setForeground(text);

    btnLogin.setText("Login");
    btnLogin.setBackground(accent);
    btnLogin.setForeground(Color.WHITE);
    btnLogin.setFocusPainted(false);

    javax.swing.JPanel mainPanel = new javax.swing.JPanel();
    mainPanel.setOpaque(false);
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(mainPanel);
    mainPanel.setLayout(layout);

    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(30)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTitle)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(radStudent)
                                    .addGap(10)
                                    .addComponent(radRegistrar)
                                    .addGap(10)
                                    .addComponent(radAccountant))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        layout
                                            .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2))
                                    .addGap(20)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(
                                                txtUser,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                190,
                                                Short.MAX_VALUE)
                                            .addComponent(txtPass)))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGap(80)
                                    .addComponent(
                                        btnLogin,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        120,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(30)));

    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout.createSequentialGroup()
                    .addGap(25)
                    .addComponent(jLabelTitle)
                    .addGap(10)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radStudent)
                            .addComponent(radRegistrar)
                            .addComponent(radAccountant))
                    .addGap(20)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(
                                txtUser,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                30,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(15)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(
                                txtPass,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                30,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(25)
                    .addComponent(
                        btnLogin,
                        javax.swing.GroupLayout.PREFERRED_SIZE,
                        35,
                        javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(25)));

    getContentPane().setLayout(new java.awt.BorderLayout());
    GradientHeaderPanel header = new GradientHeaderPanel("Student Fees Tracker");
    getContentPane().add(header, java.awt.BorderLayout.NORTH);
    getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(null);
  }

  private javax.swing.JButton btnLogin;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabelTitle;
  private javax.swing.JPasswordField txtPass;
  private javax.swing.JTextField txtUser;
  private javax.swing.JRadioButton radStudent;
  private javax.swing.JRadioButton radRegistrar;
  private javax.swing.JRadioButton radAccountant;
  private javax.swing.ButtonGroup roleGroup;
}

