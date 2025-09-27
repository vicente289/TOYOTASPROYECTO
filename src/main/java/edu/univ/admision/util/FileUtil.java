package edu.univ.admision.util;

import java.io.*; import java.nio.channels.FileChannel;

public class FileUtil {
  public static File copyToUploads(File src) throws Exception {
    File uploads = new File("uploads"); if (!uploads.exists()) uploads.mkdirs();
    File dst = new File(uploads, System.currentTimeMillis() + "_" + src.getName());
    try (FileChannel in = new FileInputStream(src).getChannel(); FileChannel out = new FileOutputStream(dst).getChannel()) {
      in.transferTo(0, in.size(), out);
    }
    return dst;
  }
  public static String mimeFromName(String name) {
    String n = name.toLowerCase();
    if (n.endsWith(".pdf")) return "application/pdf";
    if (n.endsWith(".jpg") || n.endsWith(".jpeg")) return "image/jpeg";
    if (n.endsWith(".png")) return "image/png";
    return "application/octet-stream";
  }
}
