package com.elv.web.util;

import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 访问返回工具
 *
 * @author lxh
 * @since 2020-08-04
 */
public class ResponseUtil {

    private ResponseUtil() {
    }

    public static ModelAndView forward(String url) {
        return new ModelAndView("forward:" + url);
    }

    public static ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }

    public static ModelAndView redirect(String url, Map<String, ?> paramMap) {
        return new ModelAndView("redirect:" + url, paramMap);
    }

    public static ModelAndView rendering(String viewName, String modelName, Object modelObject) {
        return new ModelAndView(viewName, modelName, modelObject);
    }

    public static ModelAndView renderingToJson(String modelName, Object modelObject) {
        return new ModelAndView(new MappingJackson2JsonView(), modelName, modelObject);
    }
}
