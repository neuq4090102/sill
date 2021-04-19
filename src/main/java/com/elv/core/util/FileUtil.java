package com.elv.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;

/**
 * @author lxh
 * @since 2020-06-16
 */
public final class FileUtil {

    /**
     * PDFBox
     *
     * @see <a href="https://pdfbox.apache.org"/>
     * @see <a href="https://iowiki.com/pdfbox/pdfbox_quick_guide.html"/>
     * <p>
     * iText
     * @see <a href="https://itextpdf.com/en"/>
     */
    private void pdfBox() {

    }

    /**
     * 循环冗余校验码
     * <p>
     * 主要用来检测或者校验数据传输后是否出现错误
     *
     * @param bytes 字节数组数据
     * @return java.lang.String
     */
    public static String fetchCrc(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return Long.toHexString(crc32.getValue());
    }

    /**
     * 文件md5
     *
     * @param filePath 文件目录
     * @return java.lang.String
     */
    public static String md5(String filePath) {
        DigestInputStream dis = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            dis = new DigestInputStream(fis, MessageDigest.getInstance("md5"));
            int length = 1024;
            byte[] buffer = new byte[length];
            int read = dis.read(buffer, 0, length);
            while (read > -1) {
                read = dis.read(buffer, 0, length);
            }
            return SecurityUtil.toHex(dis.getMessageDigest().digest());
        } catch (Exception e) {
            throw new RuntimeException("FileUtil#md5 error.", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException("FileUtil#md5 close fis error.", e);
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    throw new RuntimeException("FileUtil#md5 close dis error.", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(fetchCrc("abc".getBytes()));
        System.out.println(md5("/users/lxh/Documents/text/test.txt")); // 623e40983909b67f9167f35ea329e22c
    }
}
