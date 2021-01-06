package com.elv.core.tool.email.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lxh
 * @since 2020-05-31
 */
public class MailParam {

    @NotNull(message = "收件人不能为空")
    private List<String> recipients; // 收件人
    private List<String> ccs; //抄送人
    @NotBlank(message = "邮件主题必传")
    private String subject; // 邮件主题
    private Object content = ""; // 邮箱内容
    private List<String> filePaths; // 附件路径列表-可空

    public MailParam() {
    }

    public MailParam(List<String> recipients, String subject) {
        this.recipients = recipients;
        this.subject = subject;
    }

    public MailParam(List<String> recipients, String subject, Object content) {
        this.recipients = recipients;
        this.subject = subject;
        this.content = content;
    }

    private MailParam(Builder builder) {
        this.setRecipients(builder.getRecipients());
        this.setCcs(builder.getCcs());
        this.setSubject(builder.getSubject());
        this.setContent(builder.getContent());
        this.setFilePaths(builder.getFilePaths());
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCcs() {
        return ccs;
    }

    public void setCcs(List<String> ccs) {
        this.ccs = ccs;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> recipients; // 收件人
        private List<String> ccs; //抄送人
        private String subject; // 邮件主题
        private Object content = ""; // 邮箱内容
        private List<String> filePaths; // 附件路径列表-可空

        public List<String> getRecipients() {
            return recipients;
        }

        public Builder recipients(List<String> recipients) {
            this.recipients = recipients;
            return this;
        }

        public List<String> getCcs() {
            return ccs;
        }

        public Builder ccs(List<String> ccs) {
            this.ccs = ccs;
            return this;
        }

        public String getSubject() {
            return subject;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Object getContent() {
            return content;
        }

        public Builder content(Object content) {
            this.content = content;
            return this;
        }

        public List<String> getFilePaths() {
            return filePaths;
        }

        public Builder filePaths(List<String> filePaths) {
            this.filePaths = filePaths;
            return this;
        }

        public MailParam build() {
            return new MailParam(this);
        }

    }
}
