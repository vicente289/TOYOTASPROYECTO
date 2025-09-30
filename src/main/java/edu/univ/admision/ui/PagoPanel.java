package edu.univ.admision.ui;

import edu.univ.admision.service.PagoService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.math.BigDecimal;
import static edu.univ.admision.ui.Theme.*;

public class PagoPanel extends JPanel {
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfCI = new JTextField();
    private final JTextField tfMonto = new JTextField("100.00");
    private final JComboBox<String> cbMetodo =
            new JComboBox<>(new String[]{"TARJETA", "TRANSFERENCIA", "VENTANILLA"});
    private final JComboBox<String> cbEstado =
            new JComboBox<>(new String[]{"CONFIRMADO", "RECHAZADO", "PENDIENTE"});
    private final PagoService service = new PagoService();
    private final JButton btnContinuar = new JButton("Continuar a Carrera");
    private final Nav nav;

    public PagoPanel(Nav nav) {
        this.nav = nav;

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setOpaque(false);

        // Header estilo RegistroPanel
        JPanel header = createHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        // Card con formulario + acciones
        JPanel card = createFormCard();
        mainPanel.add(card, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // ======= UI building (estilo consistente) =======

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Pago de Inscripción");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 41, 59));

        JLabel subtitulo = new JLabel("Ingresa los datos de pago para completar tu registro");
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

        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 24, 0);
        gc.weightx = 1.0;
        gc.gridx = 0;

        // Campos (estilo de RegistroPanel)
        gc.gridy = 0;
        JLabel lblEmail = new JLabel("Correo electrónico (registrado) *");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEmail.setForeground(new Color(51, 65, 85));
        lblEmail.setLabelFor(tfEmail);
        formContainer.add(createFormField(lblEmail, tfEmail, true), gc);

        gc.gridy = 1;
        JLabel lblCI = new JLabel("Cédula de Identidad (CI) *");
        lblCI.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCI.setForeground(new Color(51, 65, 85));
        lblCI.setLabelFor(tfCI);
        formContainer.add(createFormField(lblCI, tfCI, true), gc);

        gc.gridy = 2;
        JLabel lblMonto = new JLabel("Monto *");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setForeground(new Color(51, 65, 85));
        lblMonto.setLabelFor(tfMonto);
        formContainer.add(createFormField(lblMonto, tfMonto, true), gc);

        gc.gridy = 3;
        JLabel lblMetodo = new JLabel("Método *");
        lblMetodo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMetodo.setForeground(new Color(51, 65, 85));
        lblMetodo.setLabelFor(cbMetodo);
        formContainer.add(createFormField(lblMetodo, cbMetodo, true), gc);

        gc.gridy = 4;
        JLabel lblEstado = new JLabel("Estado (simulado) *");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstado.setForeground(new Color(51, 65, 85));
        lblEstado.setLabelFor(cbEstado);
        formContainer.add(createFormField(lblEstado, cbEstado, true), gc);

        // Tooltips de ayuda rápida
        tfEmail.setToolTipText("Correo con el que te registraste");
        tfCI.setToolTipText("Número de documento sin separadores");
        tfMonto.setToolTipText("Ejemplo: 100.00");
        cbMetodo.setToolTipText("Seleccione el método de pago");
        cbEstado.setToolTipText("Estado para pruebas (simulado)");

        card.add(formContainer, BorderLayout.CENTER);

        // Acciones
        JPanel actions = createActionsPanel();
        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFormField(JLabel label, JComponent field, boolean required) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        panel.add(label, BorderLayout.NORTH);

        if (field instanceof JTextField) {
            JTextField tf = (JTextField) field;
            tf.setPreferredSize(new Dimension(400, 42));
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(203, 213, 225), 1, true),
                    new EmptyBorder(8, 12, 8, 12)
            ));

            // Focus styles como en RegistroPanel
            tf.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(59, 130, 246), 2, true),
                            new EmptyBorder(7, 11, 7, 11)
                    ));
                }
                public void focusLost(java.awt.event.FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(new Color(203, 213, 225), 1, true),
                            new EmptyBorder(8, 12, 8, 12)
                    ));
                }
            });
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(400, 42));
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 0, 0, 0));

        JButton btnProcesar = createStyledButton("Procesar pago", new Color(59, 130, 246), Color.WHITE);
        btnProcesar.addActionListener(e -> onPagar()); // lógica intacta

        btnContinuar.setEnabled(false);
        styleButton(btnContinuar, new Color(34, 197, 94), Color.WHITE);
        btnContinuar.addActionListener(e -> nav.go(2)); // lógica intacta

        panel.add(btnProcesar);
        panel.add(btnContinuar);
        return panel;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        styleButton(btn, bg, fg);
        return btn;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            final Color originalBg = bg;
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(originalBg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(originalBg);
            }
        });
    }

    // ======= Lógica original (sin cambios funcionales) =======

    private void onPagar() {
        try {
            String path = service.realizarPago(
                    tfEmail.getText(),
                    tfCI.getText(),
                    new BigDecimal(tfMonto.getText()),
                    (String) cbMetodo.getSelectedItem(),
                    (String) cbEstado.getSelectedItem()
            );
            if (path != null) {
                JOptionPane.showMessageDialog(this,
                        "Pago CONFIRMADO.\nComprobante PDF:\n" + path,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                btnContinuar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Pago registrado con estado: " + cbEstado.getSelectedItem(),
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                btnContinuar.setEnabled(false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
