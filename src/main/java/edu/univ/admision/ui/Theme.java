package edu.univ.admision.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Theme {
  public static JPanel card(String title) {
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(200,200,200)),
        title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13)));
    p.setLayout(new BorderLayout(10,10));
    p.setBackground(Color.white);
    return p;
  }
  public static JLabel h(String text){ JLabel l=new JLabel(text); l.setFont(new Font("Segoe UI", Font.BOLD, 14)); return l; }
  public static void grow(Component c){ if(c instanceof JComponent jc) jc.setPreferredSize(new Dimension(320,32)); }
}
