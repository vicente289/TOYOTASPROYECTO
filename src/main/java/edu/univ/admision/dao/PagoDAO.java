package edu.univ.admision.dao;

import edu.univ.admision.db.DB;
import edu.univ.admision.model.Pago;
import java.math.BigDecimal;
import java.sql.*;

public class PagoDAO {
  public Pago crear(Long aspiranteId, BigDecimal monto, String metodo, String estado, String transaccion) throws SQLException {
    String sql = "INSERT INTO pago(aspirante_id,monto,metodo,estado,transaccion) VALUES (?,?,?,?,?)";
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setLong(1, aspiranteId); ps.setBigDecimal(2, monto); ps.setString(3, metodo);
      ps.setString(4, estado); ps.setString(5, transaccion);
      ps.executeUpdate();
      Pago p = new Pago();
      try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) p.id = rs.getLong(1); }
      p.aspiranteId = aspiranteId; p.monto = monto; p.metodo = metodo; p.estado = estado; p.transaccion = transaccion;
      return p;
    }
  }
  public boolean tienePagoConfirmado(Long aspiranteId) throws SQLException {
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement("SELECT 1 FROM pago WHERE aspirante_id=? AND estado='CONFIRMADO' LIMIT 1")) {
      ps.setLong(1, aspiranteId); try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
    }
  }
}
