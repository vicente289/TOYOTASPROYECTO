package edu.univ.admision.ui;

import edu.univ.admision.model.Aspirante;
import edu.univ.admision.service.RegistroService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import static edu.univ.admision.ui.Theme.*;

public class RegistroPanel extends JPanel {
    private final JTextField tfNombre = new JTextField();
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfCI = new JTextField();
    private final JTextField tfTelefono = new JTextField();
    private final RegistroService service = new RegistroService();
    private final JButton btnContinuar = new JButton("Continuar a Pago →");
    private final Nav nav;
    private Aspirante aspiranteRegistrado = null;

    public RegistroPanel(Nav nav) {
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

        JLabel titulo = new JLabel("Registro de Estudiante");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 41, 59));

        JLabel subtitulo = new JLabel("Completa el formulario para iniciar tu proceso de admisión");
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
        formContainer.add(createFormField("Nombre completo", tfNombre, true), gc);

        gc.gridy = 1;
        formContainer.add(createFormField("Correo electrónico", tfEmail, true), gc);

        gc.gridy = 2;
        formContainer.add(createFormField("Cédula de Identidad (CI)", tfCI, true), gc);

        gc.gridy = 3;
        gc.insets = new Insets(0, 0, 0, 0);
        formContainer.add(createFormField("Teléfono", tfTelefono, false), gc);

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

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 0, 0, 0));

        // Botón Registrar
        JButton btnRegistrar = createStyledButton("Registrar", new Color(59, 130, 246), Color.WHITE);
        btnRegistrar.addActionListener(e -> onRegistrar());

        // Botón Continuar
        btnContinuar.setEnabled(false);
        styleButton(btnContinuar, new Color(34, 197, 94), Color.WHITE);
        btnContinuar.addActionListener(e -> nav.go(1));

        panel.add(btnRegistrar);
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

    private void onRegistrar() {
        try {
            Aspirante a = service.registrar(
                    tfNombre.getText(),
                    tfEmail.getText(),
                    tfCI.getText(),
                    tfTelefono.getText()
            );

            aspiranteRegistrado = a;

            // Mensaje de éxito mejorado
            JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
            messagePanel.setBackground(Color.WHITE);

            JLabel iconLabel = new JLabel("✓");
            iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            iconLabel.setForeground(new Color(34, 197, 94));

            JLabel message = new JLabel("<html><b>¡Registro exitoso!</b><br/><br/>" +
                    "ID de registro: <b>" + a.id + "</b><br/>" +
                    "Se ha enviado un correo de confirmación a:<br/>" +
                    "<i>" + tfEmail.getText() + "</i></html>");
            message.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            messagePanel.add(iconLabel, BorderLayout.WEST);
            messagePanel.add(message, BorderLayout.CENTER);

            JOptionPane.showMessageDialog(
                    this,
                    messagePanel,
                    "Registro Completado",
                    JOptionPane.PLAIN_MESSAGE
            );

            btnContinuar.setEnabled(true);

        } catch (Exception ex) {
            // Mensaje de error mejorado
            JLabel errorMsg = new JLabel("<html><b>Error en el registro</b><br/><br/>" +
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