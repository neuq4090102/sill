package com.elv.tools.email;

import com.sun.mail.smtp.SMTPAddressFailedException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
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
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MailSender {

    private String mimeType = "text/html;charset=utf-8";
    private String UTF8 = "utf-8";

    private Session session = null;
    private String username = null;

    public MailSender(final String smtpHostName, int smtpPort, final String username, final String password) {
        init(smtpHostName, smtpPort, username, password);
    }

    public MailSender(final String username, final String password) {
        // 通过邮箱地址解析出smtp服务器，对大多数邮箱都实用
        String smtpHostName = "smtp." + username.split("@")[1];
        init(smtpHostName, 25, username, password);
    }

    private void init(String smtpHostName, int smtpPort, final String username, final String password) {
        this.username = username;

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHostName);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        session = Session.getInstance(props, new MailAuthenticator(username, password));
    }

    public void send(String recipient, String subject, Object content) throws AddressException, MessagingException {
        this.send(Arrays.asList(recipient), subject, content);
    }

    public void send(List<String> recipients, String subject, Object content) throws AddressException,
            MessagingException {
        // 创建Mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(username));
        // 设置收件人
        final int len = recipients.size();
        InternetAddress[] addresses = new InternetAddress[len];
        for (int i = 0; i < len; i++) {
            addresses[i] = new InternetAddress(recipients.get(i));
        }
        message.setRecipients(Message.RecipientType.TO, addresses);

        // 设置主题
        message.setSubject(subject, UTF8);
        // 设置邮件内容
        message.setContent(content.toString(), mimeType);
        // 设置发信时间
        message.setSentDate(new Date());
        // 存储邮件信息
        Transport.send(message);
    }

    public void sendWithAttachment(String recipient, String subject, Object content, String filePath)
            throws AddressException, UnsupportedEncodingException, MessagingException {
        this.sendWithAttachments(Arrays.asList(recipient), subject, content, Arrays.asList(filePath));
    }

    public void sendWithAttachments(List<String> recipients, String subject, Object content, List<String> filePaths)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        // 创建Mime类型邮件
        final MimeMessage message = new MimeMessage(session);
        try {
            // 设置发件人
            message.setFrom(new InternetAddress(username));
            // 设置收件人
            final int len = recipients.size();
            InternetAddress[] addresses = new InternetAddress[len];
            for (int i = 0; i < len; i++) {
                addresses[i] = new InternetAddress(recipients.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, addresses);

            // 设置主题
            message.setSubject(subject, UTF8);

            // 创建多重消息
            Multipart multipart = new MimeMultipart();

            // 邮件内容
            BodyPart bodyPart = null;
            if (content != null) {
                bodyPart = new MimeBodyPart();
                bodyPart.setContent(content.toString(), mimeType);
                multipart.addBodyPart(bodyPart);
            }

            // 附件
            for (String filePath : filePaths) {
                bodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                bodyPart.setDataHandler(new DataHandler(source));
                bodyPart.setFileName(MimeUtility.encodeText(filePath.substring(filePath.lastIndexOf("/") + 1), UTF8,
                        "B")); // 处理附件名称中文乱码
                multipart.addBodyPart(bodyPart);
            }

            // 设置邮件内容
            message.setContent(multipart, mimeType);
            // 设置发信时间
            message.setSentDate(new Date());
            // 存储邮件信息
            Transport.send(message);
        } catch (SendFailedException e) { // 发送失败校验是否存在无效邮箱
            if (e.getCause() instanceof SMTPAddressFailedException && e.getValidUnsentAddresses() != null) {
                message.setRecipients(Message.RecipientType.TO, e.getValidUnsentAddresses()); // 重新设定有效收件人邮箱
                Transport.send(message);
            }
        }
    }

    public static class MailAuthenticator extends Authenticator {
        String username = null;
        String password = null;

        public MailAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }

    }

    public static void main(String[] args) throws AddressException, MessagingException {
        MailSender mailSender = new MailSender("liangguorong@corp.netease.com", "123456");
        mailSender.send(Arrays.asList("recipients"), "subject", "content");
    }

}
