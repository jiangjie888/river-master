package com.river.leader.core.common.controller;

import com.river.leader.core.common.warpper.BaseControllerWrapper;
import com.river.leader.core.model.exception.ServiceException;
import com.river.leader.core.model.exception.enums.CoreExceptionEnum;
import com.river.leader.core.model.reqres.response.SuccessResponseData;
import com.river.leader.core.utils.HttpContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Objects;

/**
 * 控制器基类，封装一些控制器常用的方法
 */
public class BaseController {

    protected final String REDIRECT = "redirect:";
    protected final String FORWARD = "forward:";

    protected static SuccessResponseData SUCCESS_TIP = new SuccessResponseData();

    protected HttpServletRequest getHttpServletRequest() {
        return HttpContext.getRequest();
    }

    protected HttpServletResponse getHttpServletResponse() {
        return HttpContext.getResponse();
    }

    protected HttpSession getSession() {
        return Objects.requireNonNull(HttpContext.getRequest()).getSession();
    }

    protected HttpSession getSession(Boolean flag) {
        return Objects.requireNonNull(HttpContext.getRequest()).getSession(flag);
    }

    protected String getPara(String name) {
        return Objects.requireNonNull(HttpContext.getRequest()).getParameter(name);
    }

    protected void setAttr(String name, Object value) {
        Objects.requireNonNull(HttpContext.getRequest()).setAttribute(name, value);
    }

    /**
     * 包装一个list，让list增加额外属性
     */
    protected Object warpObject(BaseControllerWrapper warpper) {
        return warpper.wrap();
    }

    /**
     * 删除cookie
     */
    protected void deleteCookieByName(String cookieName) {
        Cookie[] cookies = this.getHttpServletRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                Cookie temp = new Cookie(cookie.getName(), "");
                temp.setMaxAge(0);
                this.getHttpServletResponse().addCookie(temp);
            }
        }
    }

    /**
     * 删除所有cookie
     */
    protected void deleteAllCookie() {
        Cookie[] cookies = this.getHttpServletRequest().getCookies();
        for (Cookie cookie : cookies) {
            Cookie temp = new Cookie(cookie.getName(), "");
            temp.setMaxAge(0);
            this.getHttpServletResponse().addCookie(temp);
        }
    }

    /**
     * 返回前台文件流
     */
    protected ResponseEntity<InputStreamResource> renderFile(String fileName, String filePath) {
        try {
            final FileInputStream inputStream = new FileInputStream(filePath);
            return renderFile(fileName, inputStream);
        } catch (FileNotFoundException e) {
            throw new ServiceException(CoreExceptionEnum.FILE_READING_ERROR);
        }
    }

    /**
     * 返回前台文件流
     */
    protected ResponseEntity<InputStreamResource> renderFile(String fileName, byte[] fileBytes) {
        return renderFile(fileName, new ByteArrayInputStream(fileBytes));
    }

    /**
     * 返回前台文件流
     */
    protected ResponseEntity<InputStreamResource> renderFile(String fileName, InputStream inputStream) {
        InputStreamResource resource = new InputStreamResource(inputStream);
        String dfileName = null;
        try {
            dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }
}
