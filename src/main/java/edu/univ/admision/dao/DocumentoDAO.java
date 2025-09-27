package edu.univ.admision.dao;

import edu.univ.admision.db.DB;
import edu.univ.admision.model.Documento;
import java.sql.*;

public class DocumentoDAO {
  public void guardar(Documento d) throws SQLException {
    String sql = "INSERT INTO documento(aspirante_id,tipo,nombre_archivo,mime,tamano_bytes,ruta) VALUES (?,?,?,?,?,?)";
    try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setLong(1, d.aspiranteId); ps.setString(2, d.tipo); ps.setString(3, d.nombreArchivo);
      ps.setString(4, d.mime); ps.setLong(5, d.tamanoBytes); ps.setString(6, d.ruta);
      ps.executeUpdate();
    }
  }
}
