//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.rbf.scan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RBFUrlClassLoader extends URLClassLoader {
  private Map<String, byte[]> classBytesMap = new HashMap(20);
  private Map<String, Class> classMap = new HashMap(20);

  public RBFUrlClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
    URL[] var3 = urls;
    int var4 = urls.length;

    for(int var5 = 0; var5 < var4; ++var5) {
      URL url = var3[var5];
      String path = url.getPath();

      try {
        JarFile jarFile = new JarFile(path);
        this.init(jarFile);
      } catch (Exception var9) {
        var9.printStackTrace();
      }
    }

  }

  public Class<?> loadClass(String name) throws ClassNotFoundException {
    if (this.findLoadedClass(name) == null) {
      return super.loadClass(name);
    } else {
      byte[] bytes = (byte[])this.classBytesMap.get(name);
      return this.defineClass(name, bytes, 0, bytes.length);
    }
  }

  private void init(JarFile jarFile) {
    Enumeration<JarEntry> en = jarFile.entries();
    InputStream input = null;
    String className = null;

    try {
      while(en.hasMoreElements()) {
        JarEntry je = (JarEntry)en.nextElement();
        String name = je.getName();
        if (name.endsWith(".class")) {
          className = name.substring(0, name.length() - 6).replaceAll("/", ".");
          input = jarFile.getInputStream(je);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          int bufferSize = 4096;
          byte[] buffer = new byte[bufferSize];
          boolean var10 = false;

          int bytesNumRead;
          while((bytesNumRead = input.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesNumRead);
          }

          byte[] classBytes = baos.toByteArray();
          this.classBytesMap.put(className, classBytes);
        }
      }
    } catch (Throwable var22) {
      System.err.printf("类载入失败：%s\n", className);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException var20) {
          var20.printStackTrace();
        }
      }

    }

    Iterator var24 = this.classBytesMap.entrySet().iterator();

    while(var24.hasNext()) {
      Entry<String, byte[]> entry = (Entry)var24.next();
      String key = (String)entry.getKey();
      Class aClass = null;

      try {
        aClass = this.loadClass(key);
        this.classMap.put(key, aClass);
      } catch (Throwable var21) {
        var21.getStackTrace();
      }
    }

  }

  public Map<String, Class> getClassMap() {
    return this.classMap;
  }
}
