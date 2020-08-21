package com.elv.core.tool.email;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.elv.core.constant.Const;
import com.elv.core.tool.email.model.MailParam;
import com.elv.core.util.Assert;
import com.elv.core.util.StrUtil;
import com.sun.mail.smtp.SMTPAddressFailedException;

public class MailSender {

    private String mimeType = "text/html;charset=utf-8";

    private String host;
    private int port;
    private String userName;
    private String password;
    private Session session;

    public MailSender() {
    }

    public MailSender(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public MailSender(String host, int port, String userName, String password) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static MailSender of() {
        return new MailSender();
    }

    public MailSender host(String host) {
        this.host = host;
        return this;
    }

    public MailSender port(int port) {
        this.port = port;
        return this;
    }

    public MailSender userName(String userName) {
        this.userName = userName;
        return this;
    }

    public MailSender password(String password) {
        this.password = password;
        return this;
    }

    private MailSender session(Session session) {
        this.session = session;
        return this;
    }

    /**
     * 邮箱初始化
     *
     * @return com.elv.core.tool.email.MailSender
     */
    public MailSender init() {
        Assert.notBlank(this.getUserName(), "MailSender#init userName not blank.");
        Assert.notBlank(this.getPassword(), "MailSender#init password not blank.");
        Assert.isTrue(!StrUtil.isEmail(this.getUserName()), "MailSender#init userName isn't mailBox.");

        Properties props = new Properties();
        props.put("mail.smtp.host", this.getHost());
        props.put("mail.smtp.port", this.getPort());
        props.put("mail.smtp.auth", "true");
        Authenticator authenticator = new MailAuthenticator(getUserName(), getPassword());
        this.session(Session.getInstance(props, authenticator));

        return this;
    }

    /**
     * 邮件发送
     *
     * @param mailParam 邮箱参数
     */
    public void send(MailParam mailParam) {
        // 创建MIME类型邮件
        final MimeMessage message = new MimeMessage(this.getSession());
        try {
            // 发件人
            Address address = new InternetAddress(this.getUserName());

            // 收件人
            Address[] toAddresses = this.fetchAddress(mailParam.getRecipients());

            // 抄送人
            Address[] ccAddresses = this.fetchAddress(mailParam.getCcs());

            // 邮件内容
            Object content = null;
            List<String> filePaths = mailParam.getFilePaths();
            if (filePaths != null && filePaths.size() > 0) { // 有附件
                // 创建多重消息
                Multipart multipart = new MimeMultipart();

                // 邮件内容
                BodyPart bodyPart = null;
                if (mailParam.getContent() != null) {
                    bodyPart = new MimeBodyPart();
                    bodyPart.setContent(mailParam.getContent().toString(), mimeType);
                    multipart.addBodyPart(bodyPart);
                }

                // 附件
                for (String filePath : filePaths) {
                    bodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(filePath);
                    bodyPart.setDataHandler(new DataHandler(source));
                    bodyPart.setFileName(MimeUtility
                            .encodeText(filePath.substring(filePath.lastIndexOf("/") + 1), Const.UTF8,
                                    "B")); // 处理附件名称中文乱码
                    multipart.addBodyPart(bodyPart);
                }

                content = multipart;
            } else {
                content = mailParam.getContent().toString();
            }

            // 设置发件人
            message.setFrom(address);
            // 设置收件人，TO表示发送，CC表示抄送，BCC表示密送
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            if (ccAddresses != null) {
                // 设置抄送人
                message.setRecipients(Message.RecipientType.CC, ccAddresses);
            }
            // 设置主题
            message.setSubject(mailParam.getSubject(), Const.UTF8);
            // 设置邮件内容
            message.setContent(content, mimeType);
            // 设置发信时间
            message.setSentDate(new Date());
            // 邮件发送
            Transport.send(message);
        } catch (SendFailedException e) { // 发送失败校验是否存在无效邮箱
            try {
                if (e.getCause() instanceof SMTPAddressFailedException) {
                    if (e.getValidUnsentAddresses() != null && e.getValidUnsentAddresses().length > 0) {
                        message.setRecipients(Message.RecipientType.TO, e.getValidUnsentAddresses()); // 重新设定有效收件人邮箱
                        Transport.send(message);
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("MailSender#send failed to resend email.", e2);
            }
        } catch (Exception e) {
            throw new RuntimeException("MailSender#send failed to send email.", e);
        }
    }

    private Address[] fetchAddress(List<String> recipients) throws AddressException {
        InternetAddress[] addresses = null;
        if (recipients != null && recipients.size() > 0) {
            final int len = recipients.size();
            addresses = new InternetAddress[len];
            for (int i = 0; i < len; i++) {
                addresses[i] = new InternetAddress(recipients.get(i));
            }
        }
        return addresses;
    }

    public static class MailAuthenticator extends Authenticator {
        private String userName = null;
        private String password = null;

        public MailAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }
    }

}
