package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Simple reusable header with a green-to-blue gradient background and bold title text.
 */
public class GradientHeaderPanel extends JPanel {

  public GradientHeaderPanel(String title) {
    setOpaque(false);
    setLayout(new BorderLayout());
    JLabel lbl = new JLabel(title);
    lbl.setForeground(Color.WHITE);
    lbl.setFont(lbl.getFont().deriveFont(20f).deriveFont(java.awt.Font.BOLD));
    add(lbl, BorderLayout.CENTER);
    setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 24, 16, 24));
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    Color left = new Color(0, 150, 136);  // teal-ish green
    Color right = new Color(0, 152, 214); // blue
    GradientPaint gp = new GradientPaint(0, 0, left, getWidth(), 0, right);
    g2.setPaint(gp);
    g2.fillRect(0, 0, getWidth(), getHeight());
    g2.dispose();
    super.paintComponent(g);
  }
}


