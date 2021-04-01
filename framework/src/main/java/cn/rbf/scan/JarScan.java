//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.scan;

import cn.rbf.exception.JarScanException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarScan implements Scan {

  protected String jarpath;
  protected String prefix;

  public JarScan(Class<?> clzz) throws URISyntaxException {
    String allname = clzz.getName();
    String simpleName = clzz.getSimpleName();
    this.prefix = allname.substring(0, allname.length() - simpleName.length())
        .replaceAll("\\.", "/");
    this.jarpath = clzz.getResource("").getPath();
    this.jarpath = this.jarpath.substring(5);
    if (this.jarpath.contains(".jar")) {
      if (this.jarpath.contains(":")) {
        this.jarpath = this.jarpath.substring(1, this.jarpath.indexOf(".jar") + 4);
      } else {
        this.jarpath = this.jarpath.substring(0, this.jarpath.indexOf(".jar") + 4);
      }
    }

  }

  public void autoScan() {
    JarFile jarFile = null;

    try {
      jarFile = new JarFile(this.jarpath);
    } catch (IOException var7) {
      throw new JarScanException("找不到jar文件：[" + this.jarpath + "]", var7);
    }

    Enumeration entrys = jarFile.entries();

    try {
      while (entrys.hasMoreElements()) {
        JarEntry entry = (JarEntry) entrys.nextElement();
        String name = entry.getName();
        if (name.endsWith(".class") && name.startsWith(this.prefix)) {
          name = name.substring(0, name.length() - 6);
          String clzzName = name.replaceAll("/", "\\.");
          Class<?> fileClass = Class.forName(clzzName);
          Scanner.scan(fileClass);
        }
      }
    } catch (ClassNotFoundException var8) {
      var8.printStackTrace();
    }

  }
}
