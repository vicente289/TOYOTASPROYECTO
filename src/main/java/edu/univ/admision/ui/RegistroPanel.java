package edu.univ.admision.ui;

import edu.univ.admision.model.Aspirante; 
import edu.univ.admision.service.RegistroService;
import javax.swing.*; 
import java.awt.*;
import static edu.univ.admision.ui.Theme.*;

public class RegistroPanel extends JPanel {
  private final JTextField tfNombre=new JTextField(), tfEmail=new JTextField(), tfCI=new JTextField(), tfTelefono=new JTextField();
  private final RegistroService service = new RegistroService();
  private final JButton btnContinuar = new JButton("Continuar a Pago");
  private final Nav nav;

  public RegistroPanel(Nav nav) {
    this.nav = nav;
    setLayout(new BorderLayout(16,16));
    setBackground(new Color(245,247,250));

    JPanel card = card("HU1: Registro de Estudiante");
    JPanel form = new JPanel(new GridBagLayout());
    form.setOpaque(false);
    GridBagConstraints gc = new GridBagConstraints();
    gc.insets = new Insets(6,6,6,6); gc.anchor = GridBagConstraints.WEST; gc.fill = GridBagConstraints.HORIZONTAL;
    gc.gridx=0; gc.gridy=0; form.add(new JLabel("Nombre*"), gc); gc.gridx=1; grow(tfNombre); form.add(tfNombre, gc);
    gc.gridx=0; gc.gridy=1; form.add(new JLabel("Email*"), gc); gc.gridx=1; grow(tfEmail); form.add(tfEmail, gc);
    gc.gridx=0; gc.gridy=2; form.add(new JLabel("CI*"), gc); gc.gridx=1; grow(tfCI); form.add(tfCI, gc);
    gc.gridx=0; gc.gridy=3; form.add(new JLabel("Teléfono"), gc); gc.gridx=1; grow(tfTelefono); form.add(tfTelefono, gc);

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnReg = new JButton("Registrar"); btnReg.addActionListener(e -> onRegistrar());
    btnContinuar.setEnabled(false);
    btnContinuar.addActionListener(e -> nav.go(1));
    actions.add(btnReg); actions.add(btnContinuar);

    card.add(form, BorderLayout.CENTER);
    card.add(actions, BorderLayout.SOUTH);
    add(card, BorderLayout.CENTER);
  }

  private void onRegistrar() {
    try {
      Aspirante a = service.registrar(tfNombre.getText(), tfEmail.getText(), tfCI.getText(), tfTelefono.getText());
      JOptionPane.showMessageDialog(this, "Registrado con ID: " + a.id + "\nRevisa tu correo.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      btnContinuar.setEnabled(true);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
  }
}
