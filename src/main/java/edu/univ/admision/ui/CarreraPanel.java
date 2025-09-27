package edu.univ.admision.ui;

import edu.univ.admision.model.Carrera; import edu.univ.admision.service.CarreraService;
import javax.swing.*; import java.awt.*; import java.util.List;
import static edu.univ.admision.ui.Theme.*;

public class CarreraPanel extends JPanel {
  private final JTextField tfEmail=new JTextField(), tfCI=new JTextField();
  private final JComboBox<Carrera> cbCarrera = new JComboBox<>();
  private final CarreraService service = new CarreraService();
  private final JButton btnContinuar = new JButton("Continuar a Documentos");
  private final Nav nav;

  public CarreraPanel(Nav nav) {
    this.nav = nav;
    setLayout(new BorderLayout(16,16));
    setBackground(new Color(245,247,250));

    JPanel card = card("HU3: Selección de Carrera");
    JPanel form = new JPanel(new GridBagLayout()); form.setOpaque(false);
    GridBagConstraints gc = new GridBagConstraints();
    gc.insets=new Insets(6,6,6,6); gc.anchor=GridBagConstraints.WEST; gc.fill=GridBagConstraints.HORIZONTAL;

    gc.gridx=0; gc.gridy=0; form.add(new JLabel("Email (registrado)"), gc); gc.gridx=1; grow(tfEmail); form.add(tfEmail, gc);
    gc.gridx=0; gc.gridy=1; form.add(new JLabel("CI"), gc); gc.gridx=1; grow(tfCI); form.add(tfCI, gc);
    gc.gridx=0; gc.gridy=2; form.add(new JLabel("Carrera"), gc); gc.gridx=1; form.add(cbCarrera, gc);

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSel = new JButton("Seleccionar carrera"); btnSel.addActionListener(e -> onSelect());
    actions.add(btnSel);
    btnContinuar.setEnabled(false);
    btnContinuar.addActionListener(e -> nav.go(3));
    actions.add(btnContinuar);

    card.add(form, BorderLayout.CENTER);
    card.add(actions, BorderLayout.SOUTH);
    add(card, BorderLayout.CENTER);

    onLoad();
  }

  private void onLoad() {
    try {
      cbCarrera.removeAllItems();
      List<Carrera> list = service.listarActivas();
      if (list.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay carreras activas en la BD. Cierra y abre la app para que el bootstrap las inserte.", "Aviso", JOptionPane.WARNING_MESSAGE);
      } else {
        for (Carrera c : list) cbCarrera.addItem(c);
      }
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
  }

  private void onSelect() {
    try {
      Carrera c = (Carrera) cbCarrera.getSelectedItem(); if (c == null) throw new IllegalArgumentException("Seleccione una carrera");
      service.seleccionar(tfEmail.getText(), tfCI.getText(), c.id);
      JOptionPane.showMessageDialog(this, "Carrera seleccionada: " + c.nombre, "Éxito", JOptionPane.INFORMATION_MESSAGE);
      btnContinuar.setEnabled(true);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
  }
}
