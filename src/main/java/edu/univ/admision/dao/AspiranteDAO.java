package edu.univ.admision.dao;

import edu.univ.admision.db.DB;
import edu.univ.admision.model.Aspirante;
import java.sql.*;

public class AspiranteDAO {
  public Aspirante crear(Aspirante a) throws SQLException {
    String sql = "INSERT INTO aspirante(nombre, email, ci, telefono) VALUES (?,?,?,?)";
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, a.nombre); ps.setString(2, a.email); ps.setString(3, a.ci); ps.setString(4, a.telefono);
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) a.id = rs.getLong(1); }
      return a;
    }
  }
  public boolean emailExiste(String email) throws SQLException {
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT 1 FROM aspirante WHERE email=?")) {
      ps.setString(1, email); try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
    }
  }
  public Aspirante porEmailYCI(String email, String ci) throws SQLException {
    String sql = "SELECT id,nombre,email,ci,telefono FROM aspirante WHERE email=? AND ci=?";
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setString(1, email); ps.setString(2, ci);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return new Aspirante(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
                                            rs.getString("ci"), rs.getString("telefono"));
        return null;
      }
    }
  }
}
