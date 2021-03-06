package cn.rbf.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;


public abstract class FileUtils extends LuckyFile{

    //---------------------------------------------------------------------
    // Copy methods for java.io.File
    //---------------------------------------------------------------------

    /**
     * Copy the contents of the given input File to the given output File.
     *
     * @param in  the file to copy from
     * @param out the file to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(File in, File out) throws IOException {
        notNull(in, "No input File specified");
        notNull(out, "No output File specified");
        return copy(Files.newInputStream(in.toPath()), Files.newOutputStream(out.toPath()));
    }

    /**
     * Copy the contents of the given byte array to the given output File.
     *
     * @param in  the byte array to copy from
     * @param out the file to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, File out) throws IOException {
        notNull(in, "No input byte array specified");
        notNull(out, "No output File specified");
        copy(new ByteArrayInputStream(in), Files.newOutputStream(out.toPath()));
    }

    /**
     * Copy the contents of the given input File into a new byte array.
     *
     * @param in the file to copy from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(File in) throws IOException {
        notNull(in, "No input File specified");
        return copyToByteArray(Files.newInputStream(in.toPath()));
    }


    //---------------------------------------------------------------------
    // Copy methods for java.io.InputStream / java.io.OutputStream
    //---------------------------------------------------------------------

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Closes both streams when done.
     *
     * @param in  the stream to copy from
     * @param out the stream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        notNull(in, "No InputStream specified");
        notNull(out, "No OutputStream specified");

        try {
            return copyBase(in, out);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given byte array to the given OutputStream.
     * Closes the stream when done.
     *
     * @param in  the byte array to copy from
     * @param out the OutputStream to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        notNull(in, "No input byte array specified");
        notNull(out, "No OutputStream specified");

        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    /**
     * Copy the contents of the given InputStream into a new byte array.
     * Closes the stream when done.
     *
     * @param in the stream to copy from (may be {@code null} or empty)
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }


    //---------------------------------------------------------------------
    // Copy methods for java.io.Reader / java.io.Writer
    //---------------------------------------------------------------------

    /**
     * Copy the contents of the given Reader to the given Writer.
     * Closes both when done.
     *
     * @param in  the Reader to copy from
     * @param out the Writer to copy to
     * @return the number of characters copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(Reader in, Writer out) throws IOException {
        notNull(in, "No Reader specified");
        notNull(out, "No Writer specified");

        try {
            int byteCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given String to the given output Writer.
     * Closes the writer when done.
     *
     * @param in  the String to copy from
     * @param out the Writer to copy to
     * @throws IOException in case of I/O errors
     */
    public static void copy(String in, Writer out) throws IOException {
        notNull(in, "No input String specified");
        notNull(out, "No Writer specified");

        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given Reader into a String.
     * Closes the reader when done.
     *
     * @param in the reader to copy from (may be {@code null} or empty)
     * @return the String that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static String copyToString(Reader in) throws IOException {
        if (in == null) {
            return "";
        }

        StringWriter out = new StringWriter();
        copy(in, out);
        return out.toString();
    }

    private static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Leaves both streams open when done.
     *
     * @param in  the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copyBase(InputStream in, OutputStream out) throws IOException {
        notNull(in, "No InputStream specified");
        notNull(out, "No OutputStream specified");

        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * ????????????,?????????????????????????????????????????????????????????
     * @param fromFiles ??????????????????????????????
     * @param toFolder ???????????????
     * @throws IOException
     */
    public static void copyFolders(List<File> fromFiles,File toFolder) throws IOException {
        for (File fromFile : fromFiles) {
            copyFolder(fromFile,toFolder);
        }
    }

    /**
     * ????????????????????????????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????
     *     formDir:
     *          src
     *             |_main
     *                  |_java
     *                  |_resource
     *             |_test
     *             |_pom.xml
     *      toDir:
     *          fileCopy
     *
     *      ????????????
     *       toDir -->
     *       fileCopy
     *              |
     *              src
     *                  |_main
     *                      |_java
     *                      |_resource
     *                  |_test
     *                  |_pom.xml
     *
     * @param fromDir ?????????/?????????
     * @param toDir ???????????????
     */
    public static void copyFolder(File fromDir, File toDir) throws IOException {
        if(fromDir.isDirectory()){
            File toFolder=new File(toDir+File.separator+fromDir.getName());
            copyFiles(fromDir,toFolder);
            return;
        }
        copyFiles(fromDir,toDir);
    }


    /**
     * ???????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????
     *
     *     formDir:
     *          src
     *             |_main
     *                  |_java
     *                  |_resource
     *             |_test
     *             |_pom.xml
     *      toDir:
     *          fileCopy
     *
     *      ????????????
     *       toDir -->
     *       fileCopy
     *             |_main
     *                  |_java
     *                  |_resource
     *             |_test
     *             |_pom.xml
     *
     * @param fromDir ?????????/?????????
     * @param toDir ???????????????
     * @throws IOException
     */
    public static void copyFiles(File fromDir, File toDir) throws IOException {
        if(!fromDir.exists())
            throw new RuntimeException("?????????????????????: "+fromDir+" ,???????????????");

        //????????????????????????????????????
        if (!fromDir.isDirectory()) {
            FileOutputStream o=new FileOutputStream(toDir.getAbsoluteFile()+File.separator+fromDir.getName());
            FileInputStream i=new FileInputStream(fromDir);
            copy(i,o);
            return;
        }
        //???????????????????????????
        if (!toDir.exists()) {
            //??????????????????
            toDir.mkdirs();
        }
        //?????????????????????File????????????
        File[] files = fromDir.listFiles();
        for (File file : files) {
            //????????????fromDir(fromFile)???toDir(toFile)?????????
            String strFrom = fromDir + File.separator + file.getName();
            File from = new File(strFrom);
            String strTo = toDir + File.separator + file.getName();
            File to = new File(strTo);
            //??????File???????????????????????????
            //?????????????????????
            if (file.isDirectory()) {
                //?????????????????????????????????
                copyFiles(from, to);
            }
            if (file.isFile()) {
                copy(new FileInputStream(from), new FileOutputStream(to));
            }
        }
    }

    /**
     * ????????????????????????????????????????????????
     * @param folder
     */
    public static void deleteFile(File folder){
        File[] files = folder.listFiles();
        for (File f : files) {
            if(f.isFile()){
                f.delete();
            }else{
                deleteFile(f);
            }
        }
        folder.delete();
    }
}



