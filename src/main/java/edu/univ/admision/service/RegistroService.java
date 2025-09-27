package edu.univ.admision.service;

import edu.univ.admision.dao.AspiranteDAO;
import edu.univ.admision.model.Aspirante;
import edu.univ.admision.util.EmailUtil;

public class RegistroService {
  private final AspiranteDAO dao = new AspiranteDAO();
  public Aspirante registrar(String nombre, String email, String ci, String telefono) throws Exception {
    if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre es obligatorio");
    if (email == null || email.isBlank()) throw new IllegalArgumentException("Email es obligatorio");
    if (ci == null || ci.isBlank()) throw new IllegalArgumentException("CI es obligatorio");
    if (dao.emailExiste(email.toLowerCase().trim())) throw new IllegalArgumentException("El correo ya está registrado");
    Aspirante a = new Aspirante(null, nombre.trim(), email.trim().toLowerCase(), ci.trim(), telefono==null?null:telefono.trim());
    a = dao.crear(a);
    String subject = "Confirmación de registro - Admisión";
    String body = "Hola " + a.nombre + ",\n\nTu registro fue exitoso. ID: " + a.id +
                  "\nAhora puedes realizar el pago de inscripción, seleccionar carrera y presentar el examen.\n\nSaludos.";
    EmailUtil.enviar(a.email, subject, body);
    return a;
  }
}
