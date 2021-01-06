package com.elv.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.elv.frame.constant.FrameworkError;
import com.elv.frame.exception.AbstractException;
import com.elv.web.model.ApiResult;
import com.elv.web.model.ValidationResult;
import com.elv.web.util.RequestUtil;
import com.elv.web.util.ResponseUtil;

/**
 * @author lxh
 * @since 2020-08-04
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception e) {
        if (e instanceof AbstractException && FrameworkError.isNotPrintLog(((AbstractException) e).getCode())) {
            // do-nothing.
        } else {
            logger.error("API access error, handler:{}, msg:{}, exception:{}", handler, e.getMessage(), e);
        }

        String requestURI = RequestUtil.getURI(request);

        if (requestURI.endsWith(".html")) {
            return ResponseUtil.redirect("/error.html");
        }

        ApiResult apiResult = ApiResult.error(FrameworkError.COMMON_ERROR);
        if (e instanceof AbstractException) {
            AbstractException ae = (AbstractException) e;
            apiResult.setCode(ae.getCode());
            apiResult.setMsg(ae.getMsg());
            Object error = ae.getError();
            if (error != null && error instanceof ValidationResult) {
                apiResult.setErrors((ValidationResult) error);
            }
        } else if (e.getCause() instanceof ConstraintViolationException) {
            // TODO
        }

        return ResponseUtil.rendering("", "apiResult", apiResult);
    }
}
