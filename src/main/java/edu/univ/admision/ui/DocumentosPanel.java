package edu.univ.admision.ui;

import edu.univ.admision.service.DocumentoService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import static edu.univ.admision.ui.Theme.*;

public class DocumentosPanel extends JPanel {
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfCI = new JTextField();
    private final JTextField tfArchivo = new JTextField();
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"FOTO", "CERTIFICADO", "CI"});
    private final JButton btnExaminar = new JButton("📁 Examinar...");
    private final JButton btnSubir = new JButton("Subir Documento");
    private final JButton btnExamen = new JButton("Ir a Examen →");
    private final DocumentoService service = new DocumentoService();
    private final Nav nav;

    public DocumentosPanel(Nav nav) {
        this.nav = nav;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setOpaque(false);

        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Card con el formulario
        JPanel card = createFormCard();
        mainPanel.add(card, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Carga de Documentos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 41, 59));

        JLabel subtitulo = new JLabel("Sube los documentos requeridos para completar tu proceso de admisión");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(100, 116, 139));

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 5));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        header.add(textos, BorderLayout.WEST);

        return header;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel(new BorderLayout(20, 20));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));

        // Formulario
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 24, 0);
        gc.weightx = 1.0;

        // Campos del formulario
        gc.gridx = 0; gc.gridy = 0;
        formContainer.add(createFormField("Email registrado", tfEmail, true), gc);

        gc.gridy = 1;
        formContainer.add(createFormField("Cédula de Identidad (CI)", tfCI, true), gc);

        gc.gridy = 2;
        formContainer.add(createComboField("Tipo de documento", cbTipo, true), gc);

        gc.gridy = 3;
        gc.insets = new Insets(0, 0, 0, 0);
        formContainer.add(createFileField("Archivo", tfArchivo), gc);

        card.add(formContainer, BorderLayout.CENTER);

        // Panel de botones
        JPanel actionsPanel = createActionsPanel();
        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFormField(String label, JTextField field, boolean required) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        JLabel lblField = new JLabel(label + (required ? " *" : ""));
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblField.setForeground(new Color(51, 65, 85));

        field.setPreferredSize(new Dimension(400, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        // Efecto focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(59, 130, 246), 2, true),
                        new EmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(203, 213, 225), 1, true),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        panel.add(lblField, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createComboField(String label, JComboBox<String> combo, boolean required) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        JLabel lblField = new JLabel(label + (required ? " *" : ""));
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblField.setForeground(new Color(51, 65, 85));

        combo.setPreferredSize(new Dimension(400, 42));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        panel.add(lblField, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFileField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        JLabel lblField = new JLabel(label + " (PDF, JPG, PNG • Máx. 10MB) *");
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblField.setForeground(new Color(51, 65, 85));

        // Panel para campo + botón examinar
        JPanel fieldPanel = new JPanel(new BorderLayout(12, 0));
        fieldPanel.setBackground(Color.WHITE);

        field.setPreferredSize(new Dimension(300, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setEditable(false);
        field.setBackground(new Color(248, 250, 252));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        // Botón examinar inline
        btnExaminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExaminar.setPreferredSize(new Dimension(140, 42));
        btnExaminar.setBackground(new Color(100, 116, 139));
        btnExaminar.setForeground(Color.WHITE);
        btnExaminar.setFocusPainted(false);
        btnExaminar.setBorderPainted(false);
        btnExaminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExaminar.addActionListener(e -> onBrowse());

        // Hover effect
        btnExaminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnExaminar.setBackground(new Color(71, 85, 105));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnExaminar.setBackground(new Color(100, 116, 139));
            }
        });

        fieldPanel.add(field, BorderLayout.CENTER);
        fieldPanel.add(btnExaminar, BorderLayout.EAST);

        panel.add(lblField, BorderLayout.NORTH);
        panel.add(fieldPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 0, 0, 0));

        // Botón Subir
        styleButton(btnSubir, new Color(59, 130, 246), Color.WHITE);
        btnSubir.addActionListener(e -> onUpload());

        // Botón Examen
        btnExamen.setEnabled(false);
        styleButton(btnExamen, new Color(34, 197, 94), Color.WHITE);
        btnExamen.addActionListener(e -> nav.go(4));

        panel.add(btnSubir);
        panel.add(btnExamen);

        return panel;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(180, 42));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalBg = bg;
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(originalBg.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(originalBg);
            }
        });
    }

    private void onBrowse() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".pdf") || name.endsWith(".jpg") ||
                        name.endsWith(".jpeg") || name.endsWith(".png");
            }
            public String getDescription() {
                return "Documentos (*.pdf, *.jpg, *.png)";
            }
        });

        if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            tfArchivo.setText(ch.getSelectedFile().getAbsolutePath());
        }
    }

    private void onUpload() {
        try {
            service.subir(
                    tfEmail.getText(),
                    tfCI.getText(),
                    (String)cbTipo.getSelectedItem(),
                    new File(tfArchivo.getText())
            );

            // Mensaje de éxito mejorado
            JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
            messagePanel.setBackground(Color.WHITE);

            JLabel iconLabel = new JLabel("✓");
            iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            iconLabel.setForeground(new Color(34, 197, 94));

            JLabel message = new JLabel("<html><b>¡Documento subido correctamente!</b><br/><br/>" +
                    "Tipo: <b>" + cbTipo.getSelectedItem() + "</b><br/>" +
                    "Archivo: <i>" + new File(tfArchivo.getText()).getName() + "</i></html>");
            message.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            messagePanel.add(iconLabel, BorderLayout.WEST);
            messagePanel.add(message, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(
                    this,
                    messagePanel,
                    "Éxito",
                    JOptionPane.PLAIN_MESSAGE
            );

            btnExamen.setEnabled(true);

        } catch (Exception ex) {
            JLabel errorMsg = new JLabel("<html><b>Error al subir documento</b><br/><br/>" +
                    ex.getMessage() + "</html>");
            errorMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                    this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}