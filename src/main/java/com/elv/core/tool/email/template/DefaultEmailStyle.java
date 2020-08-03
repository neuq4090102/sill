package com.elv.core.tool.email.template;

import java.util.Map;

import javax.annotation.Resource;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.elv.core.tool.email.IEmailStyle;

/**
 * @author lxh
 * @since 2020-03-30
 */
public class DefaultEmailStyle implements IEmailStyle {

    @Resource
    private TemplateEngine templateEngine;

    @Override
    public String getTemplate() {
        return rendering("template.html", null); //TODO
    }

    /**
     * 通过模板渲染内容
     *
     * @param template
     * @param dataMap
     * @return java.lang.String
     */
    private String rendering(String template, Map<String, Object> dataMap) {
        Context context = new Context();
        context.setVariables(dataMap);
        return templateEngine.process(template, context);
    }

}
