package edu.univ.admision.ui;

import edu.univ.admision.model.Opcion; import edu.univ.admision.model.Pregunta; import edu.univ.admision.service.ExamenService;
import javax.swing.*; import java.awt.*; import java.awt.event.ActionEvent; import java.util.List;
import static edu.univ.admision.ui.Theme.*;

public class ExamenPanel extends JPanel {
    private final JTextField tfEmail=new JTextField(), tfCI=new JTextField();
    private final JButton btnIniciar=new JButton("Iniciar sesión y cargar examen");
    private final JLabel lbInfo=new JLabel("No autenticado"), lbTimer=new JLabel("00:00");
    private final JTextArea taPregunta=new JTextArea(4,50); private final JPanel opcionesPanel=new JPanel(new GridLayout(0,1));
    private final JButton btnAnterior=new JButton("Anterior"), btnSiguiente=new JButton("Siguiente"),
            btnPausa=new JButton("Pausa"), btnReanudar=new JButton("Reanudar"), btnFinalizar=new JButton("Finalizar examen");

    private final ExamenService service = new ExamenService();
    private ExamenService.Sesion sesion; private List<Pregunta> preguntas; private int idx=-1;
    private ButtonGroup grupoOpciones = new ButtonGroup(); private javax.swing.Timer timer; private int segundosRestantes=0;
    private Long opcionSeleccionadaActual=null;

    public ExamenPanel() {
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(245,247,250));

        JPanel card = card("HU8: Presentar examen");
        JPanel north = new JPanel(new GridLayout(0,2,8,8)); north.setOpaque(false);
        north.add(new JLabel("Email (registrado)")); north.add(tfEmail);
        north.add(new JLabel("CI")); north.add(tfCI);
        north.add(btnIniciar); north.add(lbInfo);
        card.add(north, BorderLayout.NORTH);

        taPregunta.setEditable(false); taPregunta.setLineWrap(true); taPregunta.setWrapStyleWord(true);
        JPanel center = new JPanel(new BorderLayout(8,8)); center.setOpaque(false);
        center.add(new JScrollPane(taPregunta), BorderLayout.NORTH); center.add(opcionesPanel, BorderLayout.CENTER);
        card.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT)); south.setOpaque(false);
        south.add(new JLabel("Tiempo:")); south.add(lbTimer);
        south.add(btnAnterior); south.add(btnSiguiente); south.add(btnPausa); south.add(btnReanudar); south.add(btnFinalizar);
        card.add(south, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        btnIniciar.addActionListener(this::onIniciar);
        btnAnterior.addActionListener(e -> mostrarPregunta(idx-1));
        btnSiguiente.addActionListener(e -> mostrarPregunta(idx+1));
        btnPausa.addActionListener(e -> { if (timer!=null) timer.stop(); });
        btnReanudar.addActionListener(e -> { if (timer!=null) timer.start(); });
        btnFinalizar.addActionListener(e -> finalizar());
        setControlesHabilitados(false);
    }

    private void onIniciar(ActionEvent e) {
        try {
            sesion = service.iniciarSesionExamen(tfEmail.getText(), tfCI.getText());
            lbInfo.setText("Aspirante: " + sesion.aspirante.nombre + " | Examen: " + sesion.examen.nombre);
            preguntas = service.preguntas(sesion.examen.id); segundosRestantes = sesion.examen.duracionMin * 60;
            iniciarTimer(); mostrarPregunta(0); setControlesHabilitados(true);
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Acceso denegado", JOptionPane.ERROR_MESSAGE); }
    }

    private void iniciarTimer() {
        actualizarTimerLabel();
        timer = new javax.swing.Timer(1000, ev -> {
            segundosRestantes--; actualizarTimerLabel();
            if (segundosRestantes <= 0) {
                ((javax.swing.Timer) ev.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Tiempo finalizado. Se enviará el examen.", "Tiempo", JOptionPane.INFORMATION_MESSAGE);
                finalizar();
            }
        });
        timer.start();
    }

    private void actualizarTimerLabel() {
        int min = Math.max(0, segundosRestantes)/60, seg=Math.max(0, segundosRestantes)%60;
        lbTimer.setText(String.format("%02d:%02d", min, seg));
    }
    private void setControlesHabilitados(boolean enabled) {
        btnAnterior.setEnabled(enabled); btnSiguiente.setEnabled(enabled); btnPausa.setEnabled(enabled);
        btnReanudar.setEnabled(enabled); btnFinalizar.setEnabled(enabled);
    }

    private void mostrarPregunta(int nuevaIdx) {
        if (preguntas==null || preguntas.isEmpty()) return;
        if (nuevaIdx<0 || nuevaIdx>=preguntas.size()) return;
        idx = nuevaIdx; Pregunta p = preguntas.get(idx);
        taPregunta.setText("(" + (idx+1) + "/" + preguntas.size() + ") " + p.enunciado);

        opcionesPanel.removeAll(); grupoOpciones = new ButtonGroup(); opcionSeleccionadaActual = null;
        try {
            java.util.List<Opcion> opciones = service.opciones(p.id);
            for (Opcion o : opciones) {
                JRadioButton rb = new JRadioButton(o.texto); rb.setActionCommand("" + o.id);
                rb.addActionListener(e -> { opcionSeleccionadaActual = Long.parseLong(e.getActionCommand()); guardarAuto(); });
                grupoOpciones.add(rb); opcionesPanel.add(rb);
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        opcionesPanel.revalidate(); opcionesPanel.repaint();
    }

    private void guardarAuto() {
        try { Pregunta p = preguntas.get(idx);
            service.guardarRespuesta(sesion.examen.id, p.id, sesion.aspirante.id, opcionSeleccionadaActual, null);
        } catch (Exception ex) { System.err.println("Auto-guardado falló: " + ex.getMessage()); }
    }

    private void finalizar() {
        try { if (timer!=null) timer.stop();
            int correctas = service.finalizarYCalificar(sesion.examen.id, sesion.aspirante.id);
            int total = preguntas.size();
            JOptionPane.showMessageDialog(this, "Examen finalizado. Puntaje: " + correctas + " / " + total, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            setControlesHabilitados(false);
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }
}