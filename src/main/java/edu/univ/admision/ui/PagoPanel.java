package edu.univ.admision.ui;

import edu.univ.admision.service.PagoService;
import javax.swing.*; import java.awt.*; import java.math.BigDecimal;
import static edu.univ.admision.ui.Theme.*;

public class PagoPanel extends JPanel {
  private final JTextField tfEmail=new JTextField(), tfCI=new JTextField(), tfMonto=new JTextField("100.00");
  private final JComboBox<String> cbMetodo=new JComboBox<>(new String[]{"TARJETA","TRANSFERENCIA","VENTANILLA"});
  private final JComboBox<String> cbEstado=new JComboBox<>(new String[]{"CONFIRMADO","RECHAZADO","PENDIENTE"});
  private final PagoService service = new PagoService();
  private final JButton btnContinuar = new JButton("Continuar a Carrera");
  private final Nav nav;

  public PagoPanel(Nav nav) {
    this.nav = nav;
    setLayout(new BorderLayout(16,16));
    setBackground(new Color(245,247,250));

    JPanel card = card("HU2: Pago de Inscripción");
    JPanel form = new JPanel(new GridBagLayout()); form.setOpaque(false);
    GridBagConstraints gc = new GridBagConstraints();
    gc.insets=new Insets(6,6,6,6); gc.anchor=GridBagConstraints.WEST; gc.fill=GridBagConstraints.HORIZONTAL;

    gc.gridx=0; gc.gridy=0; form.add(new JLabel("Email (registrado)"), gc); gc.gridx=1; grow(tfEmail); form.add(tfEmail, gc);
    gc.gridx=0; gc.gridy=1; form.add(new JLabel("CI"), gc); gc.gridx=1; grow(tfCI); form.add(tfCI, gc);
    gc.gridx=0; gc.gridy=2; form.add(new JLabel("Monto"), gc); gc.gridx=1; grow(tfMonto); form.add(tfMonto, gc);
    gc.gridx=0; gc.gridy=3; form.add(new JLabel("Método"), gc); gc.gridx=1; form.add(cbMetodo, gc);
    gc.gridx=0; gc.gridy=4; form.add(new JLabel("Estado (simulado)"), gc); gc.gridx=1; form.add(cbEstado, gc);

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btn = new JButton("Procesar pago"); btn.addActionListener(e -> onPagar());
    btnContinuar.setEnabled(false);
    btnContinuar.addActionListener(e -> nav.go(2));
    actions.add(btn); actions.add(btnContinuar);

    card.add(form, BorderLayout.CENTER);
    card.add(actions, BorderLayout.SOUTH);
    add(card, BorderLayout.CENTER);
  }

  private void onPagar() {
    try {
      String path = service.realizarPago(tfEmail.getText(), tfCI.getText(), new BigDecimal(tfMonto.getText()),
              (String)cbMetodo.getSelectedItem(), (String)cbEstado.getSelectedItem());
      if (path != null) {
        JOptionPane.showMessageDialog(this, "Pago CONFIRMADO.\nComprobante PDF:\n" + path, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        btnContinuar.setEnabled(true);
      } else {
        JOptionPane.showMessageDialog(this, "Pago registrado con estado: " + cbEstado.getSelectedItem(), "Info", JOptionPane.INFORMATION_MESSAGE);
        btnContinuar.setEnabled(false);
      }
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
  }
}
