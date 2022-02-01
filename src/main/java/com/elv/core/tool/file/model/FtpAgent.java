package com.elv.core.tool.file.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.elv.core.constant.Const;
import com.google.common.base.Charsets;

/**
 * @author lxh
 * @since 2021-11-29
 */
public class FtpAgent {

    private FileVisitor visitor;
    private FTPClient ftp;

    public static FtpAgent access(FileVisitor visitor) {
        FtpAgent ftpAgent = new FtpAgent();
        ftpAgent.setVisitor(visitor);
        return ftpAgent;
    }

    public FileVisitor getVisitor() {
        return visitor;
    }

    public void setVisitor(FileVisitor visitor) {
        this.visitor = visitor;
    }

    public FTPClient getFtp() {
        return ftp;
    }

    public void setFtp(FTPClient ftp) {
        this.ftp = ftp;
    }

    public boolean login() {
        try {
            ftp = new FTPClient();
            // 连接FPT服务器，设置IP及端口
            ftp.connect(visitor.getHost(), visitor.getPort());
            // 登录，设置用户名和密码
            if (!ftp.login(visitor.getUserName(), visitor.getPassword())) {
                return false;
            }

            if (!FTPReply.isPositiveCompletion(ftp.getReply())) {
                disconnect();
                return false;
            }
            this.init();
        } catch (Exception e) {
            throw new RuntimeException("FtpAgent#login failed to login.", e);
        }

        return true;
    }

    public void disconnect() {
        if (ftp == null || !ftp.isConnected()) {
            return;
        }
        try {
            ftp.disconnect();
        } catch (IOException e) {
            throw new RuntimeException("FtpAgent#disconnect failed to disconnect.", e);
        }
    }

    public String upload(String ftpPath, File uploadFile) throws IOException {
        String workingDirectory = ftp.printWorkingDirectory();
        if (uploadFile.isDirectory()) {
            for (File subFile : uploadFile.listFiles()) {
                upload(ftpPath, subFile);
            }
            return workingDirectory + File.separator + uploadFile.getName();
        }

        // 设置【被动模式】传输
        ftp.enterLocalPassiveMode();

        // 切换到目标目录
        this.changeDirectory(ftpPath);
        String targetFilePath = workingDirectory + File.separator + ftpPath;

        // 上传文件
        String remoteFilePath = targetFilePath + File.separator + uploadFile.getName();
        FileInputStream fis = new FileInputStream(uploadFile);
        if (!ftp.storeFile(new String(remoteFilePath.getBytes(Const.UTF8), "ISO-8859-1"), fis)) {
            return "";
        }

        // 切换回工作目录
        this.changeDirectory(workingDirectory);

        return targetFilePath;
    }

    public boolean download(String ftpFile, OutputStream os) throws IOException {
        return ftp.retrieveFile(new String(ftpFile.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1), os);
    }

    public InputStream fetchFile(String ftpFile) throws IOException {
        return ftp.retrieveFileStream(new String(ftpFile.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1));
    }

    private void init() throws IOException {
        if (ftp == null) {
            return;
        }
        // 设置编码集，防止中文乱码
        ftp.setControlEncoding("UTF-8");
        // 设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
    }

    private void changeDirectory(String ftpPath) throws IOException {
        // 判断FPT目标文件夹时候存在不存在则创建
        if (!ftp.changeWorkingDirectory(ftpPath)) {
            ftp.makeDirectory(ftpPath);
        }

        // 跳转目标目录
        ftp.changeWorkingDirectory(ftpPath);
    }

}
