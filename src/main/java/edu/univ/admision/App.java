package edu.univ.admision;

import edu.univ.admision.ui.MainFrame;
import javax.swing.*;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
      new MainFrame().setVisible(true);
    });
  }
}
//PRUEBITA DE SUBIDA DE REPOSITORIO
// hola mundo