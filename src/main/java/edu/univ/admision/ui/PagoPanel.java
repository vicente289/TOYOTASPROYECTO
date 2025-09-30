package edu.univ.admision.ui;

import edu.univ.admision.service.PagoService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.math.BigDecimal;

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

    // ---- Colores del diseño de tu captura ----
    private static final Color BG_MINT      = new Color(238, 250, 245); // verde muy claro
    private static final Color TEXT_LABEL   = new Color(33, 70, 105);   // azul label
    private static final Color INPUT_BG     = Color.WHITE;
    private static final Color INPUT_BORDER = new Color(0, 102, 204);   // azul borde (tipo Windows)
    private static final Color INPUT_BORDER_IDLE = new Color(140, 170, 190); // cuando no está en focus
    private static final Dimension INPUT_SIZE = new Dimension(560, 40);

    public PagoPanel(Nav nav) {
        this.nav = nav;

        // Lienzo general
        setLayout(new BorderLayout());
        setBackground(BG_MINT);
        setBorder(new EmptyBorder(32, 40, 32, 40));

        // Contenedor central con GridBag (etiqueta a la izquierda, campo a la derecha)
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(14, 0, 14, 0);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Título y subtítulo (puedes quitar si no los quieres)
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titulo = new JLabel("Pago de Inscripción");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(20, 35, 55));
        JLabel subtitulo = new JLabel("Ingresa los datos de pago para completar tu registro");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(100, 116, 139));
        header.add(titulo, BorderLayout.NORTH);
        header.add(subtitulo, BorderLayout.CENTER);
        header.setBorder(new EmptyBorder(0, 0, 18, 0));
        add(header, BorderLayout.NORTH);

        // ===== Fila por fila (como tu imagen) =====
        int row = 0;

        addRow(form, gc, row++, "Email (registrado)", tfEmail);
        addRow(form, gc, row++, "CI", tfCI);
        addRow(form, gc, row++, "Monto", tfMonto);
        addRow(form, gc, row++, "Método", cbMetodo);
        addRow(form, gc, row++, "Estado (simulado)", cbEstado);

        // Botonera
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(22, 0, 0, 0));

        JButton btnProcesar = styledButton("Procesar pago", new Color(51, 122, 255), Color.WHITE);
        btnProcesar.addActionListener(e -> onPagar()); // lógica intacta

        btnContinuar.setEnabled(false);
        styleButton(btnContinuar, new Color(34, 197, 94), Color.WHITE);
        btnContinuar.addActionListener(e -> nav.go(2)); // lógica intacta

        actions.add(btnProcesar);
        actions.add(btnContinuar);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(form, BorderLayout.CENTER);
        center.add(actions, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);

        // Tooltips (opcionales)
        tfEmail.setToolTipText("Ejemplo: correo@dominio.com");
        tfCI.setToolTipText("Solo dígitos");
        tfMonto.setToolTipText("Ejemplo: 100.00");
    }

    /** Crea una fila con etiqueta a la izquierda y componente a la derecha, con el estilo de la captura */
    private void addRow(JPanel form, GridBagConstraints gc, int row, String labelText, JComponent field) {
        // Etiqueta
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(TEXT_LABEL);

        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 0;
        gc.ipadx = 0;
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(label, BorderLayout.EAST);
        left.setPreferredSize(new Dimension(220, INPUT_SIZE.height)); // ancho fijo para columna de etiquetas
        form.add(left, gc);

        // Campo
        gc.gridx = 1;
        gc.gridy = row;
        gc.weightx = 1.0;

        if (field instanceof JTextField tf) {
            tf.setOpaque(true);
            tf.setBackground(INPUT_BG);
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            tf.setPreferredSize(INPUT_SIZE);
            tf.setMinimumSize(INPUT_SIZE);
            tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(INPUT_BORDER_IDLE, 1, true),
                    new EmptyBorder(8, 10, 8, 10)
            ));
            tf.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override public void focusGained(java.awt.event.FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(INPUT_BORDER, 2, true),
                            new EmptyBorder(7, 9, 7, 9)
                    ));
                }
                @Override public void focusLost(java.awt.event.FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(INPUT_BORDER_IDLE, 1, true),
                            new EmptyBorder(8, 10, 8, 10)
                    ));
                }
            });
        } else if (field instanceof JComboBox) {
            field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            field.setPreferredSize(INPUT_SIZE);
            field.setMinimumSize(INPUT_SIZE);
            field.setBorder(new LineBorder(new Color(222, 226, 230), 1, true));
        }

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(field, BorderLayout.CENTER);
        // Margen izquierdo para que quede como tu imagen
        right.setBorder(new EmptyBorder(0, 24, 0, 0));
        form.add(right, gc);
    }

    private JButton styledButton(String text, Color bg, Color fg) {
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
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(originalBg.darker());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(originalBg);
            }
        });
    }

    // --------- Lógica original (SIN CAMBIOS) ----------
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
