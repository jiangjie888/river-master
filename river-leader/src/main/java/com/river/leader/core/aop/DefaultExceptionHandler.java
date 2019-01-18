package com.river.leader.core.aop;


import com.river.leader.core.common.constant.BizExceptionEnum;
import com.river.leader.core.model.exception.ApiServiceException;
import com.river.leader.core.model.exception.RequestEmptyException;
import com.river.leader.core.model.exception.ServiceException;
import com.river.leader.core.model.reqres.response.ErrorResponseData;
import com.river.leader.core.model.reqres.response.ResponseData;
import com.river.leader.core.utils.HttpContext;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 */
@ControllerAdvice
@Order(-1)
public class DefaultExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * 用户未登录异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponseData unAuth(AuthenticationException e) {
        log.error("用户未登陆：", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(BizExceptionEnum.USER_NOT_EXISTED.getCode(), BizExceptionEnum.USER_NOT_EXISTED.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }

    /**
     * 账号被冻结异常
     */
    @ExceptionHandler(DisabledAccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponseData accountLocked(DisabledAccountException e) {
        String username = HttpContext.getRequest().getParameter("username");
        //LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号被冻结", getIp()));

        ErrorResponseData errorResponseData = new ErrorResponseData(BizExceptionEnum.ACCOUNT_FREEZED.getCode(), BizExceptionEnum.ACCOUNT_FREEZED.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());

        return errorResponseData;
    }

    /**
     * 账号密码错误异常
     */
    @ExceptionHandler(CredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponseData credentials(CredentialsException e, Model model) {
        log.error("账号密码错误!", e);
        String username = HttpContext.getRequest().getParameter("username");
        //LogManager.me().executeLog(LogTaskFactory.loginLog(username, "账号密码错误", getIp()));

        ErrorResponseData errorResponseData = new ErrorResponseData(BizExceptionEnum.AUTH_REQUEST_ERROR.getCode(), BizExceptionEnum.AUTH_REQUEST_ERROR.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }


    /**
     * 无权访问该资源异常
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponseData credentials(UndeclaredThrowableException e) {
        log.error("权限异常!", e);
        HttpContext.getRequest().setAttribute("tips", "权限异常");
        ErrorResponseData errorResponseData = new ErrorResponseData(BizExceptionEnum.NO_PERMITION.getCode(), BizExceptionEnum.NO_PERMITION.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }

    /**
     * 拦截各个服务的具体异常
     */
    @ExceptionHandler(ApiServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData apiService(ApiServiceException e) {
        log.error("服务具体异常:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(e.getCode(), e.getErrorMessage());
        errorResponseData.setExceptionClazz(e.getExceptionClassName());
        return errorResponseData;
    }

    /**
     * 拦截请求为空的异常
     */
    @ExceptionHandler(RequestEmptyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData emptyRequest(RequestEmptyException e) {
        ErrorResponseData errorResponseData = new ErrorResponseData(e.getCode(), e.getErrorMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData notFount(ServiceException e) {
        log.info("业务异常:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(e.getCode(), e.getErrorMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }


    /**
     * IllegalArgumentException异常处理返回json
     * 状态码:400
     */
    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseData badRequestException(IllegalArgumentException e) {
        log.error("服务BAD_REQUEST:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }

    /**
     * IllegalArgumentException异常处理返回json
     * 状态码:404
     */
    @ExceptionHandler({ NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseData noHandlerFoundException(NoHandlerFoundException e) {
        log.error("Not Found:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(HttpStatus.NOT_FOUND.value(), e.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }


    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData serverError(HttpServletRequest request,Exception e) {
        log.error("运行时未知异常:", e);
        ErrorResponseData errorResponseData = new ErrorResponseData(getStatus(request).value(),e.getMessage());
        errorResponseData.setExceptionClazz(e.getClass().getName());
        return errorResponseData;
    }
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
