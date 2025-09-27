package edu.univ.admision.ui;

import edu.univ.admision.service.BootstrapService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    super("Sistema de Admisión Universitaria");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 720);
    setLocationRelativeTo(null);

    new BootstrapService().ensureSeed();

    JTabbedPane tabs = new JTabbedPane();
    Nav nav = tabs::setSelectedIndex;

    tabs.addTab("Registro (HU1)", new RegistroPanel(nav));
    tabs.addTab("Pago (HU2)", new PagoPanel(nav));
    tabs.addTab("Carrera (HU3)", new CarreraPanel(nav));
    tabs.addTab("Documentos (HU16)", new DocumentosPanel(nav));
    tabs.addTab("Examen (HU8)", new ExamenPanel());

    setLayout(new BorderLayout());
    add(tabs, BorderLayout.CENTER);
  }
}
