package com.elv.core.tool.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.elv.core.tool.email.model.MailParam;

/**
 * @author lxh
 * @since 2020-07-31
 */
public class EmailUtil extends AbstractMailUtil {

    @Override
    public String getHost() {
        return "smtp.126.com";
    }

    @Override
    public String getUserName() {
        return "elv1729@126.com";
    }

    @Override
    public String getPassword() {
        return "OEMBSFKUMZCNUJZQS";
    }

    @Override
    public void send(MailParam mailParam) {
        this.defaultSend(mailParam);
    }

    public static void main(String[] args) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("/users/lxh/Documents/text/test.txt");
        filePaths.add("/users/lxh/Documents/text/front.txt");

        // MailParam mailParam = new MailParam(Arrays.asList("neuq4090102@126.com"), "local test", "Test");

        EmailUtil emailUtil = new EmailUtil();
        emailUtil.send(MailParam.builder().recipients(Arrays.asList("xinghua.lv@foxmail.com")).ccs(Arrays.asList("neuq4090102@126.com")).subject("local test")
                .content("66666").build());
        // emailUtil.send(MailParam.builder().recipients(Arrays.asList("neuq4090102@126.com")).subject("local test")
        //         .content("Test2").filePaths(filePaths).build());
        System.out.println("success.");
    }
}
