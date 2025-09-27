package edu.univ.admision.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
  private static String url, user, password;
  private static Properties props = new Properties();
  static {
    try (InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
      if (in == null) throw new RuntimeException("No se encontró db.properties");
      props.load(in);
      url = props.getProperty("url");
      user = props.getProperty("user");
      password = props.getProperty("password");
    } catch (Exception e) {
      throw new RuntimeException("Error cargando configuración de BD: " + e.getMessage(), e);
    }
  }
  public static Connection getConnection() throws SQLException { return DriverManager.getConnection(url, user, password); }
  public static Properties props(){ return props; }
}
