package edu.univ.admision.ui;

import edu.univ.admision.model.Opcion;
import edu.univ.admision.model.Pregunta;
import edu.univ.admision.service.ExamenService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ExamenPanel extends JPanel {
    private final JTextField tfEmail = new JTextField();
    private final JTextField tfCI = new JTextField();
    private final JButton btnIniciar = new JButton("Iniciar sesión y cargar examen");
    private final JLabel lbInfo = new JLabel("No autenticado");
    private final JLabel lbTimer = new JLabel("00:00");
    private final JTextArea taPregunta = new JTextArea(4, 50);
    private final JPanel opcionesPanel = new JPanel(new GridLayout(0, 1, 0, 8));
    private final JButton btnAnterior = new JButton("Anterior");
    private final JButton btnSiguiente = new JButton("Siguiente");
    private final JButton btnPausa = new JButton("Pausa");
    private final JButton btnReanudar = new JButton("Reanudar");
    private final JButton btnFinalizar = new JButton("Finalizar examen");

    private final ExamenService service = new ExamenService();
    private ExamenService.Sesion sesion;
    private List<Pregunta> preguntas;
    private int idx = -1;
    private ButtonGroup grupoOpciones = new ButtonGroup();
    private javax.swing.Timer timer;
    private int segundosRestantes = 0;
    private Long opcionSeleccionadaActual = null;

    public ExamenPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setOpaque(false);

        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Card principal
        JPanel card = createCard();
        mainPanel.add(card, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Event listeners (sin cambios)
        btnIniciar.addActionListener(this::onIniciar);
        btnAnterior.addActionListener(e -> mostrarPregunta(idx - 1));
        btnSiguiente.addActionListener(e -> mostrarPregunta(idx + 1));
        btnPausa.addActionListener(e -> {
            if (timer != null) timer.stop();
        });
        btnReanudar.addActionListener(e -> {
            if (timer != null) timer.start();
        });
        btnFinalizar.addActionListener(e -> finalizar());
        setControlesHabilitados(false);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel("Presentar Examen");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 41, 59));

        JLabel subtitulo = new JLabel("Inicia sesión para acceder al examen de admisión");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(100, 116, 139));

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 5));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        header.add(textos, BorderLayout.WEST);

        return header;
    }

    private JPanel createCard() {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // Panel norte (autenticación)
        JPanel north = createNorthPanel();
        card.add(north, BorderLayout.NORTH);

        // Panel centro (pregunta y opciones)
        JPanel center = createCenterPanel();
        card.add(center, BorderLayout.CENTER);

        // Panel sur (controles)
        JPanel south = createSouthPanel();
        card.add(south, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 20, 0);
        gc.weightx = 1.0;

        gc.gridx = 0;
        gc.gridy = 0;
        form.add(createFormField("Email (registrado)", tfEmail), gc);

        gc.gridy = 1;
        form.add(createFormField("CI", tfCI), gc);

        // Panel de acción
        JPanel actionPanel = new JPanel(new BorderLayout(16, 0));
        actionPanel.setOpaque(false);

        lbInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbInfo.setForeground(new Color(100, 116, 139));

        styleButton(btnIniciar, new Color(59, 130, 246), Color.WHITE);

        actionPanel.add(lbInfo, BorderLayout.WEST);
        actionPanel.add(btnIniciar, BorderLayout.EAST);

        gc.gridy = 2;
        gc.insets = new Insets(8, 0, 0, 0);
        form.add(actionPanel, gc);

        panel.add(form, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Área de pregunta
        taPregunta.setEditable(false);
        taPregunta.setLineWrap(true);
        taPregunta.setWrapStyleWord(true);
        taPregunta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taPregunta.setBackground(new Color(248, 250, 252));
        taPregunta.setBorder(new EmptyBorder(16, 16, 16, 16));

        JScrollPane scrollPregunta = new JScrollPane(taPregunta);
        scrollPregunta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        opcionesPanel.setBackground(Color.WHITE);
        opcionesPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        panel.add(scrollPregunta, BorderLayout.NORTH);
        panel.add(opcionesPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSouthPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Timer
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        timerPanel.setOpaque(false);

        JLabel lblTiempo = new JLabel("Tiempo:");
        lblTiempo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTiempo.setForeground(new Color(51, 65, 85));

        lbTimer.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbTimer.setForeground(new Color(220, 38, 38));

        timerPanel.add(lblTiempo);
        timerPanel.add(lbTimer);

        // Botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonsPanel.setOpaque(false);

        styleSecondaryButton(btnAnterior);
        styleSecondaryButton(btnSiguiente);
        styleSecondaryButton(btnPausa);
        styleSecondaryButton(btnReanudar);
        styleButton(btnFinalizar, new Color(220, 38, 38), Color.WHITE);

        buttonsPanel.add(btnAnterior);
        buttonsPanel.add(btnSiguiente);
        buttonsPanel.add(btnPausa);
        buttonsPanel.add(btnReanudar);
        buttonsPanel.add(btnFinalizar);

        panel.add(timerPanel, BorderLayout.WEST);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFormField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel lblField = new JLabel(label);
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblField.setForeground(new Color(51, 65, 85));

        field.setPreferredSize(new Dimension(400, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));

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

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setPreferredSize(new Dimension(160, 42));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));

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

    private void styleSecondaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(51, 65, 85));
        btn.setBackground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(248, 250, 252));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
    }

    // ========== LÓGICA ORIGINAL (SIN CAMBIOS) ==========

    private void onIniciar(ActionEvent e) {
        try {
            sesion = service.iniciarSesionExamen(tfEmail.getText(), tfCI.getText());
            lbInfo.setText("Aspirante: " + sesion.aspirante.nombre + " | Examen: " + sesion.examen.nombre);
            preguntas = service.preguntas(sesion.examen.id);
            segundosRestantes = sesion.examen.duracionMin * 60;
            iniciarTimer();
            mostrarPregunta(0);
            setControlesHabilitados(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso denegado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void iniciarTimer() {
        actualizarTimerLabel();
        timer = new javax.swing.Timer(1000, ev -> {
            segundosRestantes--;
            actualizarTimerLabel();
            if (segundosRestantes <= 0) {
                ((javax.swing.Timer) ev.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Tiempo finalizado. Se enviará el examen.", "Tiempo", JOptionPane.INFORMATION_MESSAGE);
                finalizar();
            }
        });
        timer.start();
    }

    private void actualizarTimerLabel() {
        int min = Math.max(0, segundosRestantes) / 60, seg = Math.max(0, segundosRestantes) % 60;
        lbTimer.setText(String.format("%02d:%02d", min, seg));
    }

    private void setControlesHabilitados(boolean enabled) {
        btnAnterior.setEnabled(enabled);
        btnSiguiente.setEnabled(enabled);
        btnPausa.setEnabled(enabled);
        btnReanudar.setEnabled(enabled);
        btnFinalizar.setEnabled(enabled);
    }

    private void mostrarPregunta(int nuevaIdx) {
        if (preguntas == null || preguntas.isEmpty()) return;
        if (nuevaIdx < 0 || nuevaIdx >= preguntas.size()) return;
        idx = nuevaIdx;
        Pregunta p = preguntas.get(idx);
        taPregunta.setText("(" + (idx + 1) + "/" + preguntas.size() + ") " + p.enunciado);

        opcionesPanel.removeAll();
        grupoOpciones = new ButtonGroup();
        opcionSeleccionadaActual = null;
        try {
            java.util.List<Opcion> opciones = service.opciones(p.id);
            for (Opcion o : opciones) {
                JRadioButton rb = new JRadioButton(o.texto);
                rb.setActionCommand("" + o.id);
                rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                rb.setBackground(Color.WHITE);
                rb.setBorder(new EmptyBorder(8, 8, 8, 8));
                rb.addActionListener(e -> {
                    opcionSeleccionadaActual = Long.parseLong(e.getActionCommand());
                    guardarAuto();
                });
                grupoOpciones.add(rb);
                opcionesPanel.add(rb);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        opcionesPanel.revalidate();
        opcionesPanel.repaint();
    }

    private void guardarAuto() {
        try {
            Pregunta p = preguntas.get(idx);
            service.guardarRespuesta(sesion.examen.id, p.id, sesion.aspirante.id, opcionSeleccionadaActual, null);
        } catch (Exception ex) {
            System.err.println("Auto-guardado falló: " + ex.getMessage());
        }
    }

    private void finalizar() {
        try {
            if (timer != null) timer.stop();
            int correctas = service.finalizarYCalificar(sesion.examen.id, sesion.aspirante.id);
            int total = preguntas.size();
            JOptionPane.showMessageDialog(this, "Examen finalizado. Puntaje: " + correctas + " / " + total, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            setControlesHabilitados(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}