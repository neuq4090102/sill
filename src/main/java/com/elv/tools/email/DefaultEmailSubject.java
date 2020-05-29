package com.elv.tools.email;

/**
 * @author lxh
 * @date 2020-03-30
 */
public class DefaultEmailSubject implements IEmailSubject {

    @Override
    public String getPrefix() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append(" <head>");
        sb.append("     <meta charset='utf-8'>");
        sb.append("     <style>");
        sb.append(
                "         body,table {font-family:'Helvetica Neue',Helvetica,Arial,sans-serif; font-size:10px; line-height:1.42857143; color:#333;}");
        sb.append(
                "         table {width:100%; max-width:100%; margin-bottom:16px; border-spacing:0; border-collapse:collapse; cellspacing:0}");
        sb.append("         th,td {border:1px dashed #000; text-align:center;}");
        sb.append("         th {background-color:#ddd; }");
        sb.append("         p {font-size:12px; font-weight:bold;}");
        sb.append("     </style>");
        sb.append(" </head>");
        sb.append(" <body>");


        return sb.toString();
    }

    @Override
    public String getSuffix() {
        StringBuilder sb = new StringBuilder();
        sb.append(" <br/>");
        sb.append(" <p>====================================================</p>");
        sb.append(" <p>注意：如果存在配置邮箱未收到邮件，请确保邮箱有效！！！</p>");
        sb.append(" <p>系统发送，请勿回复!</p>");
        sb.append(" </body>");
        sb.append("</html>");

        return sb.toString();
    }
}
