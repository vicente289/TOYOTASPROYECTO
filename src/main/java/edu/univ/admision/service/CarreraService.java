package edu.univ.admision.service;

import edu.univ.admision.dao.AspiranteDAO;
import edu.univ.admision.dao.CarreraDAO;
import edu.univ.admision.model.Aspirante;
import edu.univ.admision.model.Carrera;
import java.util.List;

public class CarreraService {
  private final CarreraDAO carreraDAO = new CarreraDAO();
  private final AspiranteDAO aspiranteDAO = new AspiranteDAO();

  public List<Carrera> listarActivas() throws Exception { return carreraDAO.listarActivas(); }
  public void seleccionar(String email, String ci, Long carreraId) throws Exception {
    Aspirante a = aspiranteDAO.porEmailYCI(email.toLowerCase().trim(), ci.trim());
    if (a == null) throw new IllegalArgumentException("Aspirante no encontrado");
    carreraDAO.seleccionarCarrera(a.id, carreraId);
  }
}
