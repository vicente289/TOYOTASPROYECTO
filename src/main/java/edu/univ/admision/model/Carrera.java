package edu.univ.admision.model;
public class Carrera {
  public Long id; public String codigo; public String nombre; public boolean activa;
  public String toString(){ return nombre + " ("+codigo+")"; }
}
