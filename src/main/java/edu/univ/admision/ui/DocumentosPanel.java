package edu.univ.admision.ui;

import edu.univ.admision.service.DocumentoService;
import javax.swing.*; import java.awt.*; import java.io.File;
import static edu.univ.admision.ui.Theme.*;

public class DocumentosPanel extends JPanel {
  private final JTextField tfEmail=new JTextField(), tfCI=new JTextField(), tfArchivo=new JTextField();
  private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"FOTO","CERTIFICADO","CI"});
  private final JButton btnExaminar = new JButton("Examinar..."), btnSubir = new JButton("Subir documento");
  private final JButton btnExamen = new JButton("Ir a Examen");
  private final DocumentoService service = new DocumentoService();
  private final Nav nav;

  public DocumentosPanel(Nav nav) {
    this.nav = nav;
    setLayout(new BorderLayout(16,16));
    setBackground(new Color(245,247,250));

    JPanel card = card("HU16: Carga de Documentos");
    JPanel form = new JPanel(new GridBagLayout()); form.setOpaque(false);
    GridBagConstraints gc = new GridBagConstraints();
    gc.insets=new Insets(6,6,6,6); gc.anchor=GridBagConstraints.WEST; gc.fill=GridBagConstraints.HORIZONTAL;

    gc.gridx=0; gc.gridy=0; form.add(new JLabel("Email (registrado)"), gc); gc.gridx=1; grow(tfEmail); form.add(tfEmail, gc);
    gc.gridx=0; gc.gridy=1; form.add(new JLabel("CI"), gc); gc.gridx=1; grow(tfCI); form.add(tfCI, gc);
    gc.gridx=0; gc.gridy=2; form.add(new JLabel("Tipo"), gc); gc.gridx=1; form.add(cbTipo, gc);
    gc.gridx=0; gc.gridy=3; form.add(new JLabel("Archivo (PDF/JPG/PNG ≤10MB)"), gc); gc.gridx=1; grow(tfArchivo); form.add(tfArchivo, gc);

    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    btnExaminar.addActionListener(e -> onBrowse()); btnSubir.addActionListener(e -> onUpload());
    btnExamen.setEnabled(false); btnExamen.addActionListener(e -> nav.go(4));
    actions.add(btnExaminar); actions.add(btnSubir); actions.add(btnExamen);

    card.add(form, BorderLayout.CENTER);
    card.add(actions, BorderLayout.SOUTH);
    add(card, BorderLayout.CENTER);
  }

  private void onBrowse() { JFileChooser ch = new JFileChooser(); if (ch.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) tfArchivo.setText(ch.getSelectedFile().getAbsolutePath()); }
  private void onUpload() {
    try {
      service.subir(tfEmail.getText(), tfCI.getText(), (String)cbTipo.getSelectedItem(), new File(tfArchivo.getText()));
      JOptionPane.showMessageDialog(this, "Documento subido correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      btnExamen.setEnabled(true);
    } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
  }
}
