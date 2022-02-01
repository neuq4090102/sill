package com.elv.core.tool.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.elv.core.tool.file.model.FileVisitor;

/**
 * @author lxh
 * @since 2021-11-09
 */
public abstract class AbstractFile {

    public static void main(String[] args) throws IOException {
        String filePath = "/opt/elv/write.txt";
        // write(filePath);
        // read(filePath);
        //
        // output(filePath);
        // input(filePath);
        // read(filePath);

        // checkAndCreate("/opt/elv/tmp/abc.txt");
        checkAndCreate("/opt/elv/tmp2/");

    }

    private static void input(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] data = new byte[100];
        while (fis.read(data, 0, data.length) != -1) {
            System.out.println(new String(data));
        }
        fis.close();
    }

    private static void output(String filePath) throws IOException {
        checkAndCreateFile(filePath);
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write("this is output.\n".getBytes());
        fos.write("this is outputddfa.".getBytes());
        fos.close();
    }

    // 文件不存在是否创建
    // 是否覆盖原文件内容
    private static void write(String filePath) throws IOException {

        checkAndCreateFile(filePath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        bw.write("this is from write.试试中文\r\n");
        bw.write("this is from write.\r\n");
        bw.write("this is from write.\r\n");
        bw.write("3");
        bw.flush();
        bw.close();
    }

    private static void read(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String str;
        while ((str = br.readLine()) != null) {
            System.out.println(str);
        }
        br.close();
    }

    private static void checkAndCreateFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        // throw new IOException("测试异常");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        file.createNewFile();
    }

    private static boolean checkAndCreate(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            return file.mkdir();
        } else if (!file.getParentFile().exists()) {
            boolean mkdir = file.getParentFile().mkdir();
            if (!mkdir) {
                return false;
            }
            return file.createNewFile();
        }
        return false;
    }

}
