package edu.univ.admision.service;

import edu.univ.admision.db.DB;
import java.sql.*;

public class BootstrapService {
  public void ensureSeed() {
    try (Connection c = DB.getConnection()) {
      try (Statement st = c.createStatement();
           ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM carrera")) {
        rs.next();
        if (rs.getInt(1) == 0) {
          st.executeUpdate("INSERT INTO carrera(codigo,nombre,activa) VALUES" +
            "('SIS','Ingeniería de Sistemas',1)," +
            "('CIV','Ingeniería Civil',1)," +
            "('ADM','Administración de Empresas',1)," +
            "('DER','Derecho',1)");
        }
      }
      try (Statement st = c.createStatement();
           ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM examen WHERE activo=1")) {
        rs.next();
        if (rs.getInt(1) == 0) {
          st.executeUpdate("INSERT INTO examen(nombre,duracion_min,activo) VALUES('Examen General de Admisión',20,1)");
          long exId = 0;
          try (ResultSet rs2 = st.executeQuery("SELECT id FROM examen WHERE activo=1 LIMIT 1")) { if (rs2.next()) exId = rs2.getLong(1); }
          st.executeUpdate("INSERT INTO pregunta(examen_id,enunciado,tipo) VALUES ("+exId+",'¿Cuál es 2+2?','OPCION_MULTIPLE')");
          st.executeUpdate("INSERT INTO pregunta(examen_id,enunciado,tipo) VALUES ("+exId+",'Capital de Bolivia:','OPCION_MULTIPLE')");
          st.executeUpdate("INSERT INTO pregunta(examen_id,enunciado,tipo) VALUES ("+exId+",'¿Qué es H2O?','OPCION_MULTIPLE')");
          st.executeUpdate("INSERT INTO pregunta(examen_id,enunciado,tipo) VALUES ("+exId+",'Sinónimo de rápido:','OPCION_MULTIPLE')");
          st.executeUpdate("INSERT INTO pregunta(examen_id,enunciado,tipo) VALUES ("+exId+",'¿Cuántos continentes hay?','OPCION_MULTIPLE')");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'3',0 FROM pregunta WHERE enunciado='¿Cuál es 2+2?'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'4',1 FROM pregunta WHERE enunciado='¿Cuál es 2+2?'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'Sucre/La Paz',1 FROM pregunta WHERE enunciado='Capital de Bolivia:'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'Santa Cruz',0 FROM pregunta WHERE enunciado='Capital de Bolivia:'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'Agua',1 FROM pregunta WHERE enunciado='¿Qué es H2O?'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'Oxígeno',0 FROM pregunta WHERE enunciado='¿Qué es H2O?'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'Veloz',1 FROM pregunta WHERE enunciado='Sinónimo de rápido:'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'Lento',0 FROM pregunta WHERE enunciado='Sinónimo de rápido:'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'6',1 FROM pregunta WHERE enunciado='¿Cuántos continentes hay?'");
          st.executeUpdate("INSERT INTO opcion(pregunta_id,texto,correcta) SELECT id,'5',0 FROM pregunta WHERE enunciado='¿Cuántos continentes hay?'");
        }
      }
    } catch (SQLException e) {
      System.err.println("Bootstrap falló: " + e.getMessage());
    }
  }
}
