package com.elv.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.elv.frame.constant.FrameworkError;
import com.elv.frame.exception.AbstractException;
import com.elv.web.model.ApiResult;
import com.elv.web.util.ResponseUtil;

/**
 * @author lxh
 * @since 2020-08-04
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception e) {
        logger.error("API access error, handler:{}, msg:{}, exception:{}", handler, e.getMessage(), e);

        String requestUri = urlPathHelper.getRequestUri(request);
        if (requestUri.endsWith(".html")) {
            return ResponseUtil.redirect("/error.html");
        }

        ApiResult apiResult = ApiResult.error(FrameworkError.COMMON_ERROR);
        if (e instanceof AbstractException) {
            AbstractException ae = (AbstractException) e;
            apiResult.setCode(ae.getCode());
            apiResult.setMsg(ae.getMsg());
        } else if (e.getCause() instanceof ConstraintViolationException) {
            // TODO
        }

        return ResponseUtil.renderingToJson("apiResult", apiResult);
    }
}
