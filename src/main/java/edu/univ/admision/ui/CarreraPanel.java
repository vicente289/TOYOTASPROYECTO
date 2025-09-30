package edu.univ.admision.ui;

import edu.univ.admision.model.Carrera;
import edu.univ.admision.service.CarreraService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class CarreraPanel extends JPanel {
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfCI = new JTextField();
    private final JComboBox<Carrera> cbCarrera = new JComboBox<>();
    private final CarreraService service = new CarreraService();
    private final JButton btnContinuar = new JButton("Continuar a Documentos");
    private final Nav nav;

    // Estilos
    private static final Color BG        = Color.WHITE;
    private static final Color TEXT_DARK = new Color(30, 41, 59);
    private static final Color TEXT_MUTE = new Color(100, 116, 139);
    private static final Color LABEL_CLR = new Color(51, 65, 85);
    private static final Color BORDER_IDLE  = new Color(203, 213, 225);
    private static final Color BORDER_FOCUS = new Color(59, 130, 246);

    public CarreraPanel(Nav nav) {
        this.nav = nav;

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setOpaque(false);

        // Header
        main.add(createHeader(), BorderLayout.NORTH);

        // Card
        main.add(createCard(), BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);

        onLoad(); // lógica intacta
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Selección de Carrera");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(TEXT_DARK);

        JLabel subtitulo = new JLabel("Elige la carrera para continuar con tu proceso");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(TEXT_MUTE);

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 5));
        text.setOpaque(false);
        text.add(titulo);
        text.add(subtitulo);

        header.add(text, BorderLayout.WEST);
        return header;
    }

    private JPanel createCard() {
        JPanel card = new JPanel(new BorderLayout(20, 20));
        card.setBackground(BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));

        // Formulario con etiquetas a la izquierda
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(14, 0, 14, 0);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addRow(form, gc, row++, " Correo electrónico (registrado) *", tfEmail);
        addRow(form, gc, row++, "Cédula de Identidad (CI) *", tfCI);
        addRow(form, gc, row++, "Carrera *", cbCarrera);

        // Acciones
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actions.setBackground(BG);
        actions.setBorder(new EmptyBorder(24, 0, 0, 0));

        JButton btnSel = createStyledButton("Seleccionar carrera", new Color(59, 130, 246), Color.WHITE);
        btnSel.addActionListener(e -> onSelect()); // lógica intacta

        btnContinuar.setEnabled(false);
        styleButton(btnContinuar, new Color(34, 197, 94), Color.WHITE);
        btnContinuar.addActionListener(e -> nav.go(3)); // lógica intacta

        actions.add(btnSel);
        actions.add(btnContinuar);

        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    /** Fila con etiqueta a la izquierda y campo a la derecha, estilo consistente */
    private void addRow(JPanel parent, GridBagConstraints gc, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(LABEL_CLR);

        // Columna izquierda (etiqueta)
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(label, BorderLayout.EAST);
        left.setPreferredSize(new Dimension(220, 40));
        parent.add(left, gc);

        // Columna derecha (campo)
        gc.gridx = 1; gc.gridy = row; gc.weightx = 1.0;

        if (field instanceof JTextField tf) {
            tf.setOpaque(true);
            tf.setBackground(Color.WHITE);
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            tf.setPreferredSize(new Dimension(560, 40));
            tf.setMinimumSize(new Dimension(560, 40));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_IDLE, 1, true),
                    new EmptyBorder(8, 12, 8, 12)
            ));
            tf.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override public void focusGained(java.awt.event.FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(BORDER_FOCUS, 2, true),
                            new EmptyBorder(7, 11, 7, 11)
                    ));
                }
                @Override public void focusLost(java.awt.event.FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            new LineBorder(BORDER_IDLE, 1, true),
                            new EmptyBorder(8, 12, 8, 12)
                    ));
                }
            });
        } else if (field instanceof JComboBox) {
            field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            field.setPreferredSize(new Dimension(560, 40));
            field.setMinimumSize(new Dimension(560, 40));
            field.setBorder(new LineBorder(new Color(222, 226, 230), 1, true));
        }

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(field, BorderLayout.CENTER);
        right.setBorder(new EmptyBorder(0, 24, 0, 0)); // separación respecto a la etiqueta
        parent.add(right, gc);
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
        btn.setPreferredSize(new Dimension(220, 42));
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

    // ---------------- LÓGICA ORIGINAL (sin cambios) ----------------
    private void onLoad() {
        try {
            cbCarrera.removeAllItems();
            List<Carrera> list = service.listarActivas();
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay carreras activas en la BD. Cierra y abre la app para que el bootstrap las inserte.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                for (Carrera c : list) cbCarrera.addItem(c);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSelect() {
        try {
            Carrera c = (Carrera) cbCarrera.getSelectedItem();
            if (c == null) throw new IllegalArgumentException("Seleccione una carrera");
            service.seleccionar(tfEmail.getText(), tfCI.getText(), c.id);
            JOptionPane.showMessageDialog(this, "Carrera seleccionada: " + c.nombre,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            btnContinuar.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

