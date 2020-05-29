package com.elv.tools.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lxh
 * @date 2020-03-30
 */
public class EmailHandler {

    private final static Logger logger = LoggerFactory.getLogger(EmailHandler.class);

    private String emailHost;
    private String emailPort;
    private String emailAccount;
    private String emailPassword;
    private IEmailSubject emailSubject;

    public EmailHandler() {
    }

    public EmailHandler(String emailHost, String emailPort, String emailAccount, String emailPassword) {
        this.emailHost = emailHost;
        this.emailPort = emailPort;
        this.emailAccount = emailAccount;
        this.emailPassword = emailPassword;
        emailSubject = new DefaultEmailSubject();
    }

    public EmailHandler(String emailHost, String emailPort, String emailAccount, String emailPassword,
            IEmailSubject emailSubject) {
        this.emailHost = emailHost;
        this.emailPort = emailPort;
        this.emailAccount = emailAccount;
        this.emailPassword = emailPassword;
        this.emailSubject = emailSubject;
    }

    /**
     * 发送邮件
     *
     * @param recipient 收件人
     * @param subject   邮件主题
     * @param content   邮件内容
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(String recipient, String subject, Object content)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        send(Arrays.asList(recipient), subject, content);
    }

    /**
     * 发送邮件
     *
     * @param recipients 收件人列表
     * @param subject    邮件主题
     * @param content    邮件内容
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(String[] recipients, String subject, Object content)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        send(Arrays.asList(recipients), subject, content);
    }

    /**
     * 发送邮件
     *
     * @param recipients 收件人列表
     * @param subject    邮件主题
     * @param content    邮件内容
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void send(List<String> recipients, String subject, Object content)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        sendWithAttachments(recipients, subject, content, new ArrayList<String>());
    }

    /**
     * 发送邮件-可带一个附件
     *
     * @param recipient 收件人
     * @param subject   邮件主题
     * @param content   邮件内容
     * @param filePath  附件路径
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendWithAttachment(String recipient, String subject, Object content, String filePath)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        sendWithAttachments(Arrays.asList(recipient), subject, content, Arrays.asList(filePath));
    }

    /**
     * 发送邮件-可带多个附件
     *
     * @param recipients 收件人列表
     * @param subject    邮件主题
     * @param content    邮件内容
     * @param filePaths  附件路径列表
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendWithAttachments(List<String> recipients, String subject, Object content, List<String> filePaths)
            throws AddressException, MessagingException, UnsupportedEncodingException {
        MailSender mailSender = new MailSender(this.getEmailHost(), Integer.parseInt(this.getEmailPort()),
                this.getEmailAccount(), this.getEmailPassword());
        mailSender.sendWithAttachments(recipients, subject, content, filePaths);
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(String emailPort) {
        this.emailPort = emailPort;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public IEmailSubject getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(IEmailSubject emailSubject) {
        this.emailSubject = emailSubject;
    }
}
