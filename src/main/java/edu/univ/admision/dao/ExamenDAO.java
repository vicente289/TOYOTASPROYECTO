package edu.univ.admision.dao;

import edu.univ.admision.db.DB;
import edu.univ.admision.model.*;
import java.sql.*; import java.util.*;

public class ExamenDAO {
  public Examen examenActivo() throws SQLException {
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT id,nombre,duracion_min,activo FROM examen WHERE activo=1 LIMIT 1");
         ResultSet rs = ps.executeQuery()) {
      if (rs.next()) { Examen ex = new Examen();
        ex.id=rs.getLong("id"); ex.nombre=rs.getString("nombre"); ex.duracionMin=rs.getInt("duracion_min"); ex.activo=rs.getBoolean("activo");
        return ex; }
      return null;
    }
  }
  public List<Pregunta> preguntasDeExamen(Long examenId) throws SQLException {
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(
            "SELECT id,examen_id,enunciado,tipo FROM pregunta WHERE examen_id=? ORDER BY id")) {
      ps.setLong(1, examenId);
      try (ResultSet rs = ps.executeQuery()) {
        List<Pregunta> list = new ArrayList<>();
        while (rs.next()) { Pregunta p = new Pregunta();
          p.id=rs.getLong("id"); p.examenId=rs.getLong("examen_id"); p.enunciado=rs.getString("enunciado"); p.tipo=rs.getString("tipo");
          list.add(p); }
        return list;
      }
    }
  }
  public List<Opcion> opcionesDePregunta(Long preguntaId) throws SQLException {
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(
            "SELECT id,pregunta_id,texto,correcta FROM opcion WHERE pregunta_id=? ORDER BY id")) {
      ps.setLong(1, preguntaId);
      try (ResultSet rs = ps.executeQuery()) {
        List<Opcion> list = new ArrayList<>();
        while (rs.next()) { Opcion o = new Opcion();
          o.id=rs.getLong("id"); o.preguntaId=rs.getLong("pregunta_id"); o.texto=rs.getString("texto"); o.correcta=rs.getBoolean("correcta");
          list.add(o); }
        return list;
      }
    }
  }
  public void upsertRespuesta(Respuesta r) throws SQLException {
    String sql = "INSERT INTO respuesta(examen_id,pregunta_id,aspirante_id,opcion_id,texto) VALUES (?,?,?,?,?) " +
                 "ON DUPLICATE KEY UPDATE opcion_id=VALUES(opcion_id), texto=VALUES(texto), guardado_en=CURRENT_TIMESTAMP";
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, r.examenId); ps.setLong(2, r.preguntaId); ps.setLong(3, r.aspiranteId);
      if (r.opcionId == null) ps.setNull(4, Types.BIGINT); else ps.setLong(4, r.opcionId);
      ps.setString(5, r.texto); ps.executeUpdate();
    }
  }
  public int calificarYGuardar(Long examenId, Long aspiranteId) throws SQLException {
    String sql = "SELECT pr.id AS pregunta_id, MAX(CASE WHEN o.correcta=1 THEN o.id END) AS opcion_correcta, " +
                 " (SELECT opcion_id FROM respuesta WHERE examen_id=? AND pregunta_id=pr.id AND aspirante_id=? ) AS opcion_marcada " +
                 "FROM pregunta pr LEFT JOIN opcion o ON o.pregunta_id=pr.id WHERE pr.examen_id=? GROUP BY pr.id";
    int correctas = 0;
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, examenId); ps.setLong(2, aspiranteId); ps.setLong(3, examenId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Long oc = rs.getLong("opcion_correcta"); if (rs.wasNull()) oc = null;
          Long om = rs.getLong("opcion_marcada"); if (rs.wasNull()) om = null;
          if (oc != null && oc.equals(om)) correctas++;
        }
      }
      String upsert = "INSERT INTO calificacion(aspirante_id,examen_id,puntaje,max_puntaje) " +
                      "VALUES (?,?,?,(SELECT COUNT(*) FROM pregunta WHERE examen_id=?)) " +
                      "ON DUPLICATE KEY UPDATE puntaje=VALUES(puntaje), max_puntaje=VALUES(max_puntaje), calificado_en=CURRENT_TIMESTAMP";
      try (PreparedStatement ps2 = c.prepareStatement(upsert)) {
        ps2.setLong(1, aspiranteId); ps2.setLong(2, examenId); ps2.setInt(3, correctas); ps2.setLong(4, examenId); ps2.executeUpdate();
      }
    }
    return correctas;
  }
}
