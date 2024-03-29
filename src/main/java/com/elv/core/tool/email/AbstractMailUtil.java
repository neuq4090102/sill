package com.elv.core.tool.email;

import com.elv.core.constant.Const;
import com.elv.core.tool.email.model.MailParam;
import com.elv.core.tool.email.model.MailSender;
import com.elv.core.util.StrUtil;

/**
 * @author lxh
 * @since 2020-04-31
 */
public abstract class AbstractMailUtil {

    /**
     * 邮箱服务器
     * <pre>
     *     smtp.126.com
     * </pre>
     *
     * @return java.lang.String
     */
    public String getHost() {
        String userName = this.getUserName();
        if (userName == null) {
            return "";
        }
        return "smtp." + userName.substring(userName.lastIndexOf("@") + 1);
    }

    /**
     * 端口，默认25
     *
     * @return int
     */
    public int getPort() {
        return Const.MAIL_PORT;
    }

    /**
     * 发件人邮箱用户
     *
     * @return java.lang.String
     */
    public abstract String getUserName();

    /**
     * 发件人邮箱密码
     * <p>
     * 如果是私人邮箱，此处应该是邮箱账户授权码
     *
     * @return java.lang.String
     */
    public abstract String getPassword();

    /**
     * 邮件发送
     *
     * @param mailParam 邮件参数
     */
    public abstract void send(MailParam mailParam);

    /**
     * 默认邮件发送
     *
     * @param mailParam 邮件参数
     */
    public void defaultSend(MailParam mailParam) {
        if (mailParam.getTos() == null || mailParam.getTos().size() == 0) {
            return;
        } else if (StrUtil.isBlank(mailParam.getSubject())) {
            return;
        }
        MailSender.of().host(getHost()).port(getPort()).userName(getUserName()).password(getPassword()).init()
                .send(mailParam);
    }

}
