package edu.univ.admision.dao;

import edu.univ.admision.db.DB;
import edu.univ.admision.model.Carrera;
import java.sql.*; import java.util.*;

public class CarreraDAO {
  public List<Carrera> listarActivas() throws SQLException {
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT id,codigo,nombre,activa FROM carrera WHERE activa=1 ORDER BY nombre");
         ResultSet rs = ps.executeQuery()) {
      List<Carrera> list = new ArrayList<>();
      while (rs.next()) { Carrera car = new Carrera();
        car.id=rs.getLong("id"); car.codigo=rs.getString("codigo"); car.nombre=rs.getString("nombre"); car.activa=rs.getBoolean("activa");
        list.add(car); }
      return list;
    }
  }
  public void seleccionarCarrera(Long aspiranteId, Long carreraId) throws SQLException {
    String upsert = "INSERT INTO aspirante_carrera(aspirante_id,carrera_id) VALUES (?,?) " +
                    "ON DUPLICATE KEY UPDATE carrera_id=VALUES(carrera_id), seleccionado_en=CURRENT_TIMESTAMP";
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(upsert)) {
      ps.setLong(1, aspiranteId); ps.setLong(2, carreraId); ps.executeUpdate();
    }
  }
  public Carrera carreraDeAspirante(Long aspiranteId) throws SQLException {
    String sql = "SELECT c.id,c.codigo,c.nombre,c.activa FROM aspirante_carrera ac JOIN carrera c ON c.id=ac.carrera_id WHERE ac.aspirante_id=?";
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, aspiranteId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) { Carrera car = new Carrera();
          car.id=rs.getLong("id"); car.codigo=rs.getString("codigo"); car.nombre=rs.getString("nombre"); car.activa=rs.getBoolean("activa");
          return car; }
        return null;
      }
    }
  }
}
