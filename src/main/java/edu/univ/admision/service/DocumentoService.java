package edu.univ.admision.service;

import edu.univ.admision.dao.AspiranteDAO;
import edu.univ.admision.dao.DocumentoDAO;
import edu.univ.admision.model.Aspirante;
import edu.univ.admision.model.Documento;
import edu.univ.admision.util.FileUtil;
import java.io.File;

public class DocumentoService {
  private final AspiranteDAO aspiranteDAO = new AspiranteDAO();
  private final DocumentoDAO documentoDAO = new DocumentoDAO();

  public void subir(String email, String ci, String tipo, File archivo) throws Exception {
    if (archivo == null || !archivo.exists()) throw new IllegalArgumentException("Archivo no válido");
    String nombre = archivo.getName().toLowerCase();
    if (!(nombre.endsWith(".pdf") || nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png")))
      throw new IllegalArgumentException("Formato no permitido (PDF/JPG/PNG)");
    long max = 10L * 1024L * 1024L; if (archivo.length() > max) throw new IllegalArgumentException("Archivo supera 10 MB");
    Aspirante a = aspiranteDAO.porEmailYCI(email.toLowerCase().trim(), ci.trim());
    if (a == null) throw new IllegalArgumentException("Aspirante no encontrado");
    File destino = FileUtil.copyToUploads(archivo);
    Documento d = new Documento();
    d.aspiranteId = a.id; d.tipo = tipo; d.nombreArchivo = archivo.getName();
    d.mime = FileUtil.mimeFromName(archivo.getName()); d.tamanoBytes = archivo.length(); d.ruta = destino.getAbsolutePath();
    documentoDAO.guardar(d);
  }
}
