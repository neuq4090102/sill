package com.elv.core.tool.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elv.core.tool.file.model.FileVisitor;
import com.elv.core.tool.file.model.FtpAgent;
import com.elv.core.util.StrUtil;

/**
 * @author lxh
 * @since 2021-11-29
 */
public class FtpUtil {

    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public static String upload(FileVisitor visitor, String ftpPath, String uploadFile) {
        if (StrUtil.isBlank(uploadFile)) {
            return "";
        }
        return upload(visitor, ftpPath, new File(uploadFile));
    }

    public static String upload(FileVisitor visitor, String ftpPath, File uploadFile) {
        FtpAgent ftpAgent = FtpAgent.access(visitor);
        try {
            if (ftpAgent.login()) {
                return ftpAgent.upload(ftpPath, uploadFile);
            }
        } catch (IOException e) {
            logger.error("FtpUtil#upload failed to upload.", e);
        } finally {
            ftpAgent.disconnect();
        }
        return "";
    }

    public static boolean download(FileVisitor visitor, String ftpFile, String localFile) {
        if (StrUtil.isBlank(localFile)) {
            return false;
        }
        try {
            return download(visitor, ftpFile, new FileOutputStream(localFile));
        } catch (FileNotFoundException e) {
            logger.error("FtpUtil#download failed to download.", e);
        }
        return false;
    }

    public static boolean download(FileVisitor visitor, String ftpFile, OutputStream os) {
        FtpAgent ftpAgent = FtpAgent.access(visitor);
        try {
            if (ftpAgent.login()) {
                return ftpAgent.download(ftpFile, os);
            }
        } catch (IOException e) {
            logger.error("FtpUtil#download failed to download.", e);
        } finally {
            ftpAgent.disconnect();
        }
        return false;
    }

    public static InputStream fetchFile(FileVisitor visitor, String ftpFile) {
        FtpAgent ftpAgent = FtpAgent.access(visitor);
        try {
            if (ftpAgent.login()) {
                return ftpAgent.fetchFile(ftpFile);
            }
        } catch (IOException e) {
            logger.error("FtpUtil#fetchFile failed to fetchFile.", e);
        } finally {
            ftpAgent.disconnect();
        }

        return null;
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

    public static void main(String[] args) {
        String host = "127.0.0.1";
        // String host = "10.188.224.145";
        FileVisitor visitor = FileVisitor.of().host(host).port(21).userName("lxh").password("abcdef");
        String ftpPath = "/opt/elv/ftp/";
        String file = "/opt/elv/write.txt";

        System.out.println(upload(visitor, ftpPath, file));
    }
}

