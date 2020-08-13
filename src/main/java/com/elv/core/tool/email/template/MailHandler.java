// package com.elv.core.tool.email.template;
//
// import java.util.Map;
//
// import javax.annotation.Resource;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.stereotype.Component;
// import org.thymeleaf.TemplateEngine;
// import org.thymeleaf.context.Context;
// import org.thymeleaf.dialect.IDialect;
// import org.thymeleaf.spring4.SpringTemplateEngine;
// import org.thymeleaf.spring4.dialect.SpringStandardDialect;
// import org.thymeleaf.templateresolver.StringTemplateResolver;
//
// /**
//  * 邮件模板业务
//  *
//  * @author lxh
//  * @since 2020-08-04
//  */
// @Component
// public class MailHandler {
//
//     @Resource
//     private TemplateEngine templateEngine;
//
//     @Resource(name = "StringTemplateEngine")
//     private TemplateEngine stringTemplateEngine;
//
//     /**
//      * 通过模板文件渲染
//      *
//      * <pre>
//      *     查看配置framework-servlet.xml#templateResolver
//      *     调用：rendering("template", map);
//      * </pre>
//      *
//      * @param templateFile 模板文件
//      * @param dataMap      数据
//      * @return java.lang.String
//      */
//     public String rendering(String templateFile, Map<String, Object> dataMap) {
//         Context context = new Context();
//         context.setVariables(dataMap);
//         return templateEngine.process(templateFile, context);
//     }
//
//     /**
//      * 通过字符串模板渲染
//      *
//      * <pre>
//      *     调用：renderingByStr("This is [(${content})]", map);
//      * </pre>
//      *
//      * @param templateStr 字符串模板
//      * @param dataMap     数据
//      * @return java.lang.String
//      */
//     public String renderingByStr(String templateStr, Map<String, Object> dataMap) {
//         Context context = new Context();
//         context.setVariables(dataMap);
//         return stringTemplateEngine.process(templateStr, context);
//     }
//
//     @Bean("StringTemplateEngine")
//     public SpringTemplateEngine StringResolver() {
//
//         IDialect dialect = new SpringStandardDialect();
//
//         // 字符串解析
//         StringTemplateResolver resolver = new StringTemplateResolver();
//         resolver.setCacheable(true);
//
//         // 模板引擎
//         SpringTemplateEngine engine = new SpringTemplateEngine();
//         engine.setDialect(dialect);
//         engine.setTemplateResolver(resolver);
//
//         return engine;
//     }
//
// }
