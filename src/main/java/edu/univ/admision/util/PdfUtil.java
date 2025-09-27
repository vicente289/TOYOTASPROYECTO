package edu.univ.admision.util;

import com.lowagie.text.Document; import com.lowagie.text.DocumentException; import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import edu.univ.admision.model.Aspirante;
import java.io.*; import java.math.BigDecimal;

public class PdfUtil {
  public static String generarComprobante(Aspirante a, BigDecimal monto, String metodo, String transaccion, String fecha) throws Exception {
    File dir = new File("receipts"); if (!dir.exists()) dir.mkdirs();
    String filename = "comprobante_" + a.id + "_" + transaccion + ".pdf"; File out = new File(dir, filename);
    try (FileOutputStream fos = new FileOutputStream(out)) {
      Document doc = new Document(); PdfWriter.getInstance(doc, fos); doc.open();
      doc.add(new Paragraph("COMPROBANTE DE PAGO - ADMISION")); doc.add(new Paragraph(" "));
      doc.add(new Paragraph("Aspirante: " + a.nombre + " (ID " + a.id + ")"));
      doc.add(new Paragraph("Email: " + a.email + " | CI: " + a.ci));
      doc.add(new Paragraph("Monto: " + monto.toPlainString())); doc.add(new Paragraph("Metodo: " + metodo));
      doc.add(new Paragraph("Transacción: " + transaccion)); doc.add(new Paragraph("Fecha: " + fecha));
      doc.close();
    } catch (DocumentException de) { throw new RuntimeException(de); }
    return out.getAbsolutePath();
  }
}
