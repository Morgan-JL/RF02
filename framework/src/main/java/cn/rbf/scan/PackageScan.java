//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.scan;

import java.io.File;
import java.net.URISyntaxException;

public class PackageScan implements Scan {

  private String rootPath;
  private String projectPath;

  public PackageScan(Class<?> application) throws URISyntaxException {
    this.rootPath = application.getClassLoader().getResource("").toURI().getPath();
    this.projectPath = this.rootPath.replaceAll("test-classes", "classes");
  }

  public void autoScan() {
    try {
      this.fileScan(this.projectPath);
    } catch (ClassNotFoundException var2) {
      var2.printStackTrace();
    }

  }

  public void fileScan(String path) throws ClassNotFoundException {
    File bin = new File(path);
    File[] listFiles = bin.listFiles();
    File[] var4 = listFiles;
    int var5 = listFiles.length;

    for (int var6 = 0; var6 < var5; ++var6) {
      File file = var4[var6];
      if (file.isDirectory()) {
        this.fileScan(path + "/" + file.getName());
      }

      if (file.getAbsolutePath().endsWith(".class")) {
        Scanner.scan(this.getFileClass(file));
      }
    }

  }

  private Class<?> getFileClass(File file) throws ClassNotFoundException {
    String className = file.getAbsolutePath().replaceAll("\\\\", "/")
        .replaceAll(this.projectPath, "").replaceAll("/", "\\.");
    className = className.substring(0, className.length() - 6);
    Class<?> fileClass = Class.forName(className.split("classes.")[1]);
    return fileClass;
  }
}
