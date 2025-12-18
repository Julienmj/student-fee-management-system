package app;

import javax.swing.SwingUtilities;
import ui.LoginForm;

/**
 * Application entry point. Launches the login form on the EDT.
 */
public class Main {

  private Main() {
  }

  public static void main(String[] args) {
    // Apply a light modern Nimbus look and feel with teal accents
    try {
      javax.swing.UIManager.put("control", new java.awt.Color(249, 251, 255));      // window/panels
      javax.swing.UIManager.put("info", new java.awt.Color(249, 251, 255));
      javax.swing.UIManager.put("nimbusBase", new java.awt.Color(0, 150, 136));     // teal
      javax.swing.UIManager.put("nimbusBlueGrey", new java.awt.Color(210, 218, 235));
      javax.swing.UIManager.put("text", new java.awt.Color(33, 37, 41));            // near-black
      for (javax.swing.UIManager.LookAndFeelInfo info :
          javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception ignored) {
      // fall back to default L&F
    }

    SwingUtilities.invokeLater(
        () -> {
          LoginForm loginForm = new LoginForm();
          loginForm.setVisible(true);
        });
  }
}

