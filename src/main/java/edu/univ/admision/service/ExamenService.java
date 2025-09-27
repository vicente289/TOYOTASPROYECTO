package edu.univ.admision.service;

import edu.univ.admision.dao.*; import edu.univ.admision.model.*;
import java.sql.SQLException; import java.util.List;

public class ExamenService {
  private final AspiranteDAO aspiranteDAO = new AspiranteDAO();
  private final PagoDAO pagoDAO = new PagoDAO();
  private final ExamenDAO examenDAO = new ExamenDAO();

  public static class Sesion { public Aspirante aspirante; public Examen examen; }

  public Sesion iniciarSesionExamen(String email, String ci) throws Exception {
    Aspirante a = aspiranteDAO.porEmailYCI(email.toLowerCase().trim(), ci.trim());
    if (a == null) throw new IllegalArgumentException("Aspirante no encontrado");
    if (!pagoDAO.tienePagoConfirmado(a.id)) throw new IllegalStateException("Acceso denegado: se requiere pago confirmado");
    Examen ex = examenDAO.examenActivo(); if (ex == null) throw new IllegalStateException("No hay examen activo");
    Sesion s = new Sesion(); s.aspirante = a; s.examen = ex; return s;
  }
  public List<Pregunta> preguntas(Long examenId) throws SQLException { return examenDAO.preguntasDeExamen(examenId); }
  public List<Opcion> opciones(Long preguntaId) throws SQLException { return examenDAO.opcionesDePregunta(preguntaId); }
  public void guardarRespuesta(Long examenId, Long preguntaId, Long aspiranteId, Long opcionId, String texto) throws SQLException {
    Respuesta r = new Respuesta(); r.examenId = examenId; r.preguntaId = preguntaId; r.aspiranteId = aspiranteId; r.opcionId = opcionId; r.texto = texto;
    examenDAO.upsertRespuesta(r);
  }
  public int finalizarYCalificar(Long examenId, Long aspiranteId) throws SQLException { return examenDAO.calificarYGuardar(examenId, aspiranteId); }
}
