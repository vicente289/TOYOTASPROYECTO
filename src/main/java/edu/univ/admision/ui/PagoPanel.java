package edu.univ.admision.ui;

import edu.univ.admision.service.PagoService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.math.BigDecimal;

public class PagoPanel extends JPanel {
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfCI = new JTextField();
    private final JTextField tfMonto = new JTextField("100.00");
    private final JComboBox<String> cbMetodo = new JComboBox<>(new String[]{"TARJETA", "TRANSFERENCIA", "VENTANILLA"});
    private final JComboBox<String> cbEstado = new JComboBox<>(new String[]{"CONFIRMADO", "RECHAZADO", "PENDIENTE"});
    private final PagoService service = new PagoService();
    private final JButton btnContinuar = new JButton("Continuar a Carrera →");
    private final Nav nav;

    public PagoPanel(Nav nav) {
        this.nav = nav;
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Panel principal con el card
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setOpaque(false);

        // Título mejorado
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

        JLabel titulo = new JLabel("Pago de Inscripción");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 41, 59));

        JLabel subtitulo = new JLabel("Completa los datos para procesar tu pago de admisión");
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
        formContainer.add(createFormField("Correo electrónico", tfEmail, true), gc);

        gc.gridy = 1;
        formContainer.add(createFormField("Cédula de Identidad (CI)", tfCI, true), gc);

        gc.gridy = 2;
        formContainer.add(createFormField("Monto (Bs.)", tfMonto, true), gc);

        gc.gridy = 3;
        formContainer.add(createComboField("Método de pago", cbMetodo, true), gc);

        gc.gridy = 4;
        gc.insets = new Insets(0, 0, 0, 0);
        formContainer.add(createComboField("Estado de pago (simulado)", cbEstado, true), gc);

        card.add(formContainer, BorderLayout.CENTER);

        // Panel de botones mejorado
        JPanel actionsPanel = createActionsPanel();
        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFormField(String label, JTextField field, boolean required) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        // Label con asterisco si es requerido
        JLabel lblField = new JLabel(label + (required ? " *" : ""));
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblField.setForeground(new Color(51, 65, 85));

        // Estilizar el campo de texto
        field.setPreferredSize(new Dimension(400, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        // Efecto hover/focus
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

        // Label con asterisco si es requerido
        JLabel lblField = new JLabel(label + (required ? " *" : ""));
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblField.setForeground(new Color(51, 65, 85));

        // Estilizar el combobox
        combo.setPreferredSize(new Dimension(400, 42));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

        panel.add(lblField, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 0, 0, 0));

        // Botón Procesar Pago
        JButton btnProcesar = createStyledButton("Procesar pago", new Color(59, 130, 246), Color.WHITE);
        btnProcesar.addActionListener(e -> onPagar());

        // Botón Continuar
        btnContinuar.setEnabled(false);
        styleButton(btnContinuar, new Color(34, 197, 94), Color.WHITE);
        btnContinuar.addActionListener(e -> nav.go(2));

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
        btn.setPreferredSize(new Dimension(160, 42));
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
                // Mensaje de éxito mejorado
                JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
                messagePanel.setBackground(Color.WHITE);

                JLabel iconLabel = new JLabel("✓");
                iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
                iconLabel.setForeground(new Color(34, 197, 94));

                JLabel message = new JLabel("<html><b>¡Pago confirmado!</b><br/><br/>" +
                        "Tu pago ha sido procesado exitosamente.<br/>" +
                        "Comprobante PDF generado en:<br/>" +
                        "<i>" + path + "</i></html>");
                message.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                messagePanel.add(iconLabel, BorderLayout.WEST);
                messagePanel.add(message, BorderLayout.CENTER);

                JOptionPane.showMessageDialog(
                        this,
                        messagePanel,
                        "Pago Exitoso",
                        JOptionPane.PLAIN_MESSAGE
                );

                btnContinuar.setEnabled(true);
            } else {
                // Mensaje informativo
                JLabel infoMsg = new JLabel("<html><b>Pago registrado</b><br/><br/>" +
                        "Estado: <b>" + cbEstado.getSelectedItem() + "</b><br/>" +
                        "El pago será procesado según el estado seleccionado.</html>");
                infoMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                JOptionPane.showMessageDialog(
                        this,
                        infoMsg,
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );

                btnContinuar.setEnabled(false);
            }
        } catch (Exception ex) {
            // Mensaje de error mejorado
            JLabel errorMsg = new JLabel("<html><b>Error al procesar el pago</b><br/><br/>" +
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