package com.elv.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.elv.core.tool.db.jdbc.BasicQuery;
import com.elv.core.tool.db.model.EmailAccount;
import com.elv.core.tool.email.AbstractMailUtil;
import com.elv.core.tool.email.model.MailParam;

/**
 * @author lxh
 * @since 2020-07-31
 */
public class MailUtil extends AbstractMailUtil {

    private MailUtil() {
    }

    public static MailUtil of() {
        return new MailUtil();
    }

    @Override
    public String getUserName() {
        return queryEmailAccount().getAccount();
    }

    @Override
    public String getPassword() {
        return queryEmailAccount().getPassword();
    }

    @Override
    public void send(MailParam mailParam) {
        this.defaultSend(mailParam);
    }

    private EmailAccount queryEmailAccount() {
        return BasicQuery.emailAccount();
    }

    public static void main(String[] args) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("/users/lxh/Documents/text/test.txt");
        filePaths.add("/users/lxh/Documents/text/front.txt");

        // MailParam mailParam = new MailParam(Arrays.asList("neuq4090102@126.com"), "local test", "Test");

        MailUtil mailUtil = MailUtil.of();
        System.out.println(mailUtil.getHost());
        mailUtil.send(MailParam.builder().recipients(Arrays.asList("neuq4090102@126.com")).subject("local test")
                .content("Just a test.").build());
        // mailUtil.send(MailParam.builder().recipients(Arrays.asList("neuq4090102@126.com")).subject("local test")
        //         .content("Test2").filePaths(filePaths).build());
        System.out.println("success.");
    }
}
