package edu.univ.admision.model;
public class Aspirante {
  public Long id; public String nombre; public String email; public String ci; public String telefono;
  public Aspirante() {}
  public Aspirante(Long id, String nombre, String email, String ci, String telefono) {
    this.id=id; this.nombre=nombre; this.email=email; this.ci=ci; this.telefono=telefono;
  }
}
