package edu.univ.admision.util;

import edu.univ.admision.db.DB;
import jakarta.mail.*; import jakarta.mail.internet.InternetAddress; import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
  public static void enviar(String to, String subject, String body) {
    try {
      Properties p = DB.props();
      String host = p.getProperty("mail.smtp.host");
      if (host == null || host.isBlank()) {
        System.out.println("[EMAIL SIMULADO] Para: " + to + " | Asunto: " + subject + "\n" + body);
        return;
      }
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", p.getProperty("mail.smtp.starttls","true"));
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", p.getProperty("mail.smtp.port","587"));
      final String user = p.getProperty("mail.smtp.user");
      final String pass = p.getProperty("mail.smtp.password");
      String from = p.getProperty("mail.from", user);

      Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(user, pass); }
      });
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(subject); message.setText(body);
      Transport.send(message);
    } catch (Exception e) {
      System.err.println("Fallo enviando correo (usando simulación): " + e.getMessage());
      System.out.println("[EMAIL SIMULADO] Para: " + to + " | Asunto: " + subject + "\n" + body);
    }
  }
}
