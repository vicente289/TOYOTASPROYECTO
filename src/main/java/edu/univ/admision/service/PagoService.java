package edu.univ.admision.service;

import edu.univ.admision.dao.AspiranteDAO;
import edu.univ.admision.dao.PagoDAO;
import edu.univ.admision.model.Aspirante;
import edu.univ.admision.util.PdfUtil;
import java.math.BigDecimal; import java.time.LocalDateTime; import java.time.format.DateTimeFormatter; import java.util.UUID;

public class PagoService {
  private final AspiranteDAO aspiranteDAO = new AspiranteDAO();
  private final PagoDAO pagoDAO = new PagoDAO();

  public String realizarPago(String email, String ci, BigDecimal monto, String metodo, String estado) throws Exception {
    if (monto == null || monto.compareTo(new BigDecimal("0")) <= 0) throw new IllegalArgumentException("Monto inválido");
    if (metodo == null) throw new IllegalArgumentException("Seleccione un método");
    Aspirante a = aspiranteDAO.porEmailYCI(email.toLowerCase().trim(), ci.trim());
    if (a == null) throw new IllegalArgumentException("Aspirante no encontrado (email/CI)");
    String transaccion = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    pagoDAO.crear(a.id, monto, metodo, estado, transaccion);
    if ("CONFIRMADO".equals(estado)) {
      String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      String path = PdfUtil.generarComprobante(a, monto, metodo, transaccion, fecha);
      return path;
    }
    return null;
  }

  public boolean tienePagoConfirmado(Long aspiranteId) throws Exception { return pagoDAO.tienePagoConfirmado(aspiranteId); }
}
